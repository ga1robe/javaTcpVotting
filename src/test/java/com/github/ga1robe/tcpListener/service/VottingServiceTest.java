package com.github.ga1robe.tcpListener.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class VottingServiceTest {

    private static Logger logger = LoggerFactory.getLogger(VottingService.class);
    //vottings by name
    private Map<String, Votting> vottings = new HashMap<String, Votting>();
    //nodes by name
    private Map<String, Node> nodes = new HashMap<String, Node>();
    //nodes by name

    @Value("#{${service.votting.time}}")
    private Integer vottingTime;
    @Value("#{${service.inactive.time}}")
    private Integer inactiveTime;

    @Autowired
    private VottingService vottingService;

    @Test
    void callNode() {
        try {
            vottingService.callNode("Node1");
            assertEquals(0, nodes.size());
            assertEquals(0, vottings.size());
        } catch (VottingException e) {
            assertEquals("", e.getMessage());
        }
    }

    @Test
    void callNew() {
        try {
            vottingService.callNode("node1");
            vottingService.callNew("node1", "votting1", true, "comment");
            assertEquals(0, nodes.size());
            assertEquals(0, vottings.size());
        } catch (VottingException e) {
            assertEquals("Node unknown. Register node first.", e.getMessage());
        }
    }

    @Test
    void callVote() {
        try {
            vottingService.callNode("node1");
            vottingService.callVote("Node1","votting1", true);
            assertEquals(0, nodes.size());
            assertEquals(0, vottings.size());
        } catch (VottingException e) {
            assertEquals("Node unknown. Register node first.", e.getMessage());
        }
    }

    @Test
    void nodeQuit() {
        try {
            vottingService.callNode("node1");
            vottingService.nodeQuit("node1");
            assertEquals(0, nodes.size());
        } catch (VottingException e) {
            assertEquals("Node unknown. Register node first.", e.getMessage());
        }
    }
}