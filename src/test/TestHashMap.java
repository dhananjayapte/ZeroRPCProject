package test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.client.Client;

public class TestHashMap {

	public static void main(String args[]) throws IOException {
		// instead of new objects try object-pool
		/*Server service = new Server();
		service.bind("tcp://*:4242");
		// service.run();
		Thread t = new Thread(service);
		t.start();*/

		Client client = new Client();
		client.connect("tcp://localhost:4242");
		
		//Map<String,String> nameMap = new HashMap<String, String>();
		Map<Object, Object> nameMap = new HashMap<Object, Object>();
		nameMap.put("0", "abc");
		nameMap.put("1", "xyz");
		nameMap.put("2", "pqr");
		for(int i=0; i<20; i++){
			client.sendHashMap("sendHashMap", nameMap);
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
