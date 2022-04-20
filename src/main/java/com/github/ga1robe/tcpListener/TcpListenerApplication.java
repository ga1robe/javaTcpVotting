package com.github.ga1robe.tcpListener;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.ga1robe.tcpListener.socket.SocketServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan("com.github.ga1robe.tcpListener")
public class TcpListenerApplication implements CommandLineRunner {
	@Value("#{${socket.server.port}}")
    private Integer serverPort;
	private static Logger logger = LoggerFactory.getLogger(TcpListenerApplication.class);

	@Lookup
	public SocketServer getSocketServer(int port) {
		return null;
	}

	

	public static void main(final String[] args) throws IOException {
		SpringApplication.run(TcpListenerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Running Spring Boot Application");
		final ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(getSocketServer(serverPort));
	}
}
