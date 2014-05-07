package test;

import java.io.IOException;

import main.client.Client;
import main.server.Server;

public class TestArray {

	public static void main(String args[]) throws IOException {
		// instead of new objects try object-pool
		Server service = new Server();
		service.bind("tcp://*:4242");
		// service.run();
		Thread t = new Thread(service);
		t.start();

		Client client = new Client();
		client.connect("tcp://localhost:4242");
		
		int numberArray[] = new int[]{1,2,3,4,5};
		client.sendNumberArray("sendNumberArray", numberArray);
	}

}
