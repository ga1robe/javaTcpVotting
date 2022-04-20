package com.github.ga1robe.tcpListener.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.ga1robe.tcpListener.TcpListenerApplication;
import com.github.ga1robe.tcpListener.service.Observer;
import com.github.ga1robe.tcpListener.service.VottingException;
import com.github.ga1robe.tcpListener.service.VottingService;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServerThread extends Thread implements Runnable,Observer {

    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    @Autowired
    private VottingService vottingService;

    private static Logger logger = LoggerFactory.getLogger(TcpListenerApplication.class);

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void receivedVote(String nodeName, String vottingName, boolean vottingChoice) {
        output.println(String.format("VOTE %s %s %s",nodeName,vottingName,vottingChoice ? 'Y' : 'N'));
    }

    @Override
    public void receivedNewVotting(String nodeName, String vottingName, String vottingContent) {
        output.println(String.format("NEW %s %s %s",nodeName,vottingName,vottingContent));
    }

    @Override
    public void vottingResult(String vottingName,boolean vottingResult) {
        output.println(String.format("RESULT %s %s",vottingName,vottingResult ? 'Y' : 'N'));
    }

    @Override
    public void run() {
        try {
            /* Reading the input from Client */
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            /* returning the output to the client :
               true statement is to flush the buffer otherwise
               we have to do it manually */
            output = new PrintWriter(socket.getOutputStream(),true);

            String nodeName = null;

            vottingService.attach(this);

            /* inifite while loop for server */
            while(true) {
                String inputString = input.readLine();
                if (inputString == null) {  //connection closed
                    if (nodeName != null) vottingService.nodeQuit(nodeName);
                    return;
                }
                /* if user types exit command */
                String[] inputWords = inputString.split(" ");
                logger.info("Server received: " + inputString);
                try {
                    if(inputString.equals("PONG")) {
                        continue;
                    } else if(inputString.equals("PING")) {
                        vottingService.callPing(nodeName);
                        output.println("PONG");
                    } else if(inputWords.length == 2 && inputWords[0].equals("NODE") && inputWords[1].length() > 0) {
                        nodeName = inputWords[1];
                        vottingService.callNode(nodeName);
                    } else if(inputWords.length == 4 && inputWords[0].equals("NEW") && inputWords[1].length() > 0 && inputWords[2].length() > 0) {
                        String vottingName = inputWords[1];
                        if (!"Y".equals(inputWords[2]) && !"N".equals(inputWords[2])) throw new VottingException("Incorrect votting value");
                        boolean vottingChoice = "Y".equals(inputWords[2]);
                        String vottingContent = inputWords[3];
                        vottingService.callNew(nodeName,vottingName,vottingChoice,vottingContent);
                    } else if(inputWords.length == 3 && inputWords[0].equals("VOTE") && inputWords[1].length() > 0 && inputWords[2].length() > 0) {
                        String vottingName = inputWords[1];
                        if (!"Y".equals(inputWords[2]) && !"N".equals(inputWords[2])) throw new VottingException("Incorrect votting value");
                        boolean vottingChoice = "Y".equals(inputWords[2]);
                        vottingService.callVote(nodeName,vottingName,vottingChoice);
                    } else {
                        output.println("NOK Incorrect operation");
                    }
                } catch (VottingException e) {
                    output.println("NOK "+e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occured " +e.getStackTrace());
        }
    }

    private void printToALlClients(String outputString) {
        for( ServerThread serverThread : threadList) {
            serverThread.output.println("REQUEST \'" + outputString + "\'");
        }

    }

    

}
