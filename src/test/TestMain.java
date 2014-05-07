package test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import main.client.Client;
import main.server.Server;

public class TestMain {

	public static void main(String[] args) throws Exception {

		Server service = new Server();
		service.bind("tcp://*:4242");
		// service.run();
		Thread t = new Thread(service);
		t.start();

		Client client = new Client();
		client.connect("tcp://localhost:4242");

		int a;
		float b;

		System.out.println("********************************** Test Class **********************************************");
		do {

			Scanner option = new Scanner(System.in);

			System.out.println("\n Main Menu :");
			System.out.println("1. Add");
			System.out.println("2. Subtract");
			System.out.println("3. Multiply");
			System.out.println("4. Divide");
			System.out.println("5. Array");
			System.out.println("6. HashMap");
			System.out.println("7. Exit");
			System.out.println("Enter option : ");
			int caseNo = option.nextInt();
			switch (caseNo) {
			case 1:
				a = 4;
				b = (float) 5.6;
				int z = 2;
				int y = 6;
				client.executeFunction("add", a, y, z);
				break;
				
			case 2:
				a = 4;
				b = 0;
				int d = 2;
				client.executeFunction("subtract", a, d);
				break;
				
			case 3:
				a = 4;
				b = 12;
				client.executeFunction("multiply", a, b);
				break;
				
			case 4:
				a = 4;
				int c = 0;
				client.executeFunction("divide", a, c);
				break;
				
			case 5:
				int numberArray[] = new int[]{1,2,3,4,5};
				client.sendNumberArray("sendNumberArray", numberArray);
				break;
				
			case 6:
				Map<Object, Object> nameMap = new HashMap<Object, Object>();
				nameMap.put("0", "abc");
				nameMap.put("1", "xyz");
				nameMap.put("2", "pqr");
				client.sendHashMap("sendHashMap", nameMap);
				break;
				
			case 7:
				System.out.println("*******************************************Thank You*****************************************************");
				System.exit(0);
			}

		} while (true);
	}
}
