package com.github.ga1robe.tcpListener.service;

import java.util.HashMap;
import java.util.Map;

public class Votting {
private String name;
private String content;
private Map<String,Boolean> votes = new HashMap<String,Boolean>();

public Votting(String name, String content) {
    this.name = name;
    this.content = content;
}

public int nrOfVotes() {
    return votes.size();
}

public void setVote(String nodeName,boolean choice) {
    votes.put(nodeName, choice);
}

public void clearVote(String nodeName) {
    votes.remove(nodeName);
}


public boolean getResult() {
    int allVotes = votes.size();
    long yesVotes = votes.values().stream().filter(result -> result).count();
    return (float)yesVotes/allVotes > 0.5;
}

public String getContent() {
    return content;
}

public void setContent(String content) {
    this.content = content;
}


public String getName() {
    return name;
}

}
