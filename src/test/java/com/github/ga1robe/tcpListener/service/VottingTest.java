package com.github.ga1robe.tcpListener.service;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VottingTest {

    @Test
    void nrOfVotes() {
        Votting votting = new Votting("votting1", "content1");
        int nrOfVotes = votting.nrOfVotes();
        assertEquals(0 , nrOfVotes);
    }

    @Test
    void setVote() {
        Votting votting = new Votting("votting1", "content1");
        votting.setVote("node1", true);
        int nrOfVotes = votting.nrOfVotes();
        assertEquals(1 , nrOfVotes);
    }

    @Test
    void clearVote() {
        Votting votting = new Votting("votting1", "content1");
        votting.setVote("node1", true);
        votting.clearVote("node1");
        int nrOfVotes = votting.nrOfVotes();
        assertEquals(0 , nrOfVotes);
    }

    @Test
    void getResult() {
        Map<String,Boolean> votes = new HashMap<String,Boolean>();
        Votting votting = new Votting("votting1", "content1");
        boolean result = votting.getResult();
        assertEquals(false, result);
    }

    @Test
    void getContent() {
        Votting votting = new Votting("votting1", "content1");
        String content = votting.getContent();
        assertEquals("content1", content);
    }

    @Test
    void setContent() {
        Votting votting = new Votting("votting1", "content0");
        votting.setContent("content1");
        String content = votting.getContent();
        assertEquals("content1", content);

    }

    @Test
    void getName() {
        Votting votting = new Votting("votting1", "content1");
        String name = votting.getName();
        assertEquals("votting1", name);
    }
}