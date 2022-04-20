package com.github.ga1robe.tcpListener.service;

public interface Observer {
    public void receivedVote(String nodeName,String vottingName,boolean vottingChoice);
    public void receivedNewVotting(String nodeName,String vottingName,String vottingContent);
    public void vottingResult(String vottingName,boolean vottingResult);
}
