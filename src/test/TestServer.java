package test;

import java.io.IOException;

import main.server.Server;

public class TestServer {

	public static void main(String args[]) throws IOException {
		// instead of new objects try object-pool
		Server service = new Server();
		service.bind("tcp://*:4242");
		// service.run();
		Thread t = new Thread(service);
		t.start();
	}
}
