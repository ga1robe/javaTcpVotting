package com.github.ga1robe.tcpListener.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.github.ga1robe.tcpListener.TcpListenerApplication;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SocketServer implements Runnable {

    private int port;
    // private Socket client;

    private ServerSocket serverSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static Logger logger = LoggerFactory.getLogger(TcpListenerApplication.class);

    public SocketServer(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
            logger.info("SocketServer Listening on port:" + port);
        } catch (IOException e) {
            logger.warn("WARNING." + "May be port " + port + " is already in use.");
            /* server socket not installed */
            this.serverSocket = null;
        }
    }

    @Lookup
    public ServerThread getServerThread(Socket socket,ArrayList<ServerThread> serverThreadList) {
        return null;
    }

    @Override
    public void run() {
        /*
         * using serversocket as argument to automatically close the socket
         * the port number is unique for each server
         * list to add all the clients node
         */
        ArrayList<ServerThread> serverThreadList = new ArrayList<>();
        try (ServerSocket serversocket = this.serverSocket) {
            while (true) {
                if (serversocket == null) throw new RuntimeException("Could not open socket");
                Socket socket = serversocket.accept();
                //ServerThread serverThread = new ServerThread(socket, serverThreadList);
                ServerThread serverThread = getServerThread(socket, serverThreadList);
                /* starting the thread */
                serverThreadList.add(serverThread);
                serverThread.start();
                /* get all the list of currently running thread */
            }
        } catch (Exception e) {
            logger.warn("Error occured in SocketServer.run: " + e.getStackTrace());
        }
    }

    /**
     * Called when disconnecting
     *
     * @throws IOException
     */
    private void disconnect() throws IOException {
        this.reader.close();
        this.writer.close();
        logger.info("Server disconnected");
        this.serverSocket.close();
    }
}