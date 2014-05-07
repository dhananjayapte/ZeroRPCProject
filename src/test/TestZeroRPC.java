package test;

import java.io.IOException;

import main.client.Client;
import main.server.Server;

public class TestZeroRPC {

	public static void main(String args[]) throws IOException {
		// instead of new objects try object-pool
		/*Server service = new Server();
		service.bind("tcp://*:4242");
		// service.run();
		Thread t = new Thread(service);
		t.start();*/

		Client client = new Client();
		client.connect("tcp://localhost:4242");
		//float a = (float) 4.3;
		/*int a = 4;
		int b = 2;
		for(int i=0; i<12; i++){
			client.executeFunction("add", a, b);
		}*/
		client.executeFunction("uploadtos3");
	}

}
