package com.github.ga1robe.tcpListener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class TcpListenerApplicationTest {

    @Test
    void testGetSocketServer() {
        TcpListenerApplication tcpListenerApplication = new TcpListenerApplication();
        tcpListenerApplication.getSocketServer(5017);
    }

    @Test
    void testRunningSpringApplication() {
        SpringApplication.run(TcpListenerApplication.class);
    }
}