package com.github.ga1robe.tcpListener.service;

public class Node {
    private String nodeName;
    private boolean active = true;
    private long lastConnection = System.currentTimeMillis();

    
    public Node(String nodeName) {
        this.nodeName = nodeName;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public long getLastConnection() {
        return lastConnection;
    }
    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    
}
