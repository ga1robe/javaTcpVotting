package com.github.ga1robe.tcpListener.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VottingService {
    private static Logger logger = LoggerFactory.getLogger(VottingService.class);
    //vottings by name
    private Map<String, Votting> vottings = new HashMap<String, Votting>();
    //nodes by name
    private Map<String, Node> nodes = new HashMap<String, Node>();
    //nodes by name
    private List<Observer> observers = new ArrayList<Observer>();

    @Value("#{${service.votting.time}}")
    private Integer vottingTime;
    @Value("#{${service.inactive.time}}")
    private Integer inactiveTime;

    private void verifyNode(String nodeName) throws VottingException {
        if (nodeName == null || !nodes.containsKey(nodeName))
            throw new VottingException("Node unknown. Register node first.");
    }

    public void callNode(String nodeName) throws VottingException {
        // same node was registered before
        if (nodes.containsKey(nodeName)) {
            Node node = nodes.get(nodeName);
            if (node.isActive()) throw new VottingException("Active node with this name already exists. Use another name.");
            logger.info("Node resumed:"+nodeName);
            node.setActive(true); // mark as active
        } else {
        //register new node
            logger.info("New node registered:"+nodeName);
            Node node = new Node(nodeName);
            nodes.put(nodeName, node);
        }
    }

    private void closeVotting(Votting votting) {
        boolean result = votting.getResult();
        logger.info("Votting closed with result:"+result);
        vottings.remove(votting.getName());
        for (Observer observer : observers) {
            observer.vottingResult(votting.getName(), result);
        }
    }

    private void closeNode(String nodeName) {
        logger.info("Clearing inactive node:"+nodeName);
        //clear node
        nodes.remove(nodeName);
        //clear choice of node
        for (Votting votting: vottings.values()) {
            votting.clearVote(nodeName);
            // if node is removed, votting can be closed
            checkIfVottingClosed(votting);
        }
    }

    private void checkIfVottingClosed(Votting votting) {
        // close votting if half of active nodes votted
        if (nodes.size() > 0 && (float)votting.nrOfVotes() / nodes.size() > 0.5) {
            closeVotting(votting);
        }
    }

    private void scheduleInactiveNodeClosure(String nodeName) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Node node = nodes.get(nodeName);
                        // verify if it is still inactive
                        if (node != null && !node.isActive()) closeNode(nodeName);
                    }
                },
                inactiveTime*1000); //vottingTime is in seconds
    }

    private void scheduleVottingClosure(Votting votting) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        //close votting if it is still active
                        if (vottings.containsKey(votting.getName()))
                            closeVotting(votting);
                    }
                },
                vottingTime*1000); //vottingTime is in seconds
    }

    public void callNew(String nodeName, String vottingName, boolean vottingChoice, String vottingContent)
            throws VottingException {
        verifyNode(nodeName);
        /* create new votting */
        Votting votting = new Votting(vottingName, vottingContent);
        /* register new votting */
        vottings.put(vottingName, votting);
        /* register votting choice */
        for (Observer observer : observers) {
            observer.receivedNewVotting(nodeName, vottingName, vottingContent);
        }
        callVote(nodeName, vottingName, vottingChoice);
        scheduleVottingClosure(votting);
    }

    public void callVote(String nodeName, String vottingName, boolean vottingChoice) throws VottingException {
        verifyNode(nodeName);
        /* retrieve votting */
        if (!vottings.containsKey(vottingName))
            throw new VottingException(String.format("Votting %s not initialized", vottingName));
        Votting votting = vottings.get(vottingName);
        votting.setVote(nodeName, vottingChoice);
        /* notify all observers */
        for (Observer observer : observers) {
            observer.receivedVote(nodeName, vottingName, vottingChoice);
        }
        checkIfVottingClosed(votting);
    }

    public void nodeQuit(String nodeName) {
        logger.info("Node quited:"+nodeName);
        Node node = nodes.get(nodeName);
        node.setLastConnection(System.currentTimeMillis());
        node.setActive(false);
        scheduleInactiveNodeClosure(nodeName);
    }

    public void callPing(String nodeName) throws VottingException {
        /* do nothing */
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }



}