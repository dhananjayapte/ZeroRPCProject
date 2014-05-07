package main.client;

//  Hello World client in Java
//  Connects REQ socket to tcp://serverip:port
//  Sends "Hello" to server, expects "World" back

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import main.command.CommandWrapper;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.type.ValueFactory;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;
import org.zeromq.ZMQ;

public class Client {
	ZMQ.Context context = null;
	ZMQ.Socket socket = null;

	public void connect(String address) {
		this.context = ZMQ.context(1);
		this.socket = context.socket(ZMQ.REQ);
		// Socket to talk to server
		this.socket.connect(address);
	}

	//generalized function which creates command and sets the methodName and the arguments
	public void executeFunction(String methodName, Object... args) throws IOException{
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final MessagePack msgpack = new MessagePack();
		
		Value[] values = new Value[args.length];
        int i = 0;
        for(Object arg : args) {
            if(arg instanceof Integer) {
                values[i++] = ValueFactory.createIntegerValue(((Integer) arg));
            } else if (arg instanceof String) {
                values[i++] = ValueFactory.createRawValue((String)arg);
            }
        }
		
        //send to server
		Packer packer = msgpack.createPacker(out);
        packer.write(new CommandWrapper(methodName, values));
        this.socket.send(out.toByteArray(), 0);
		
        //receive from server
		byte[] response = this.socket.recv(0);
		ByteArrayInputStream in = new ByteArrayInputStream(response);
        Unpacker unpacker = msgpack.createUnpacker(in);
        System.out.println("Incoming class is -->>" +unpacker.getNextType());
        if(unpacker.getNextType() == ValueType.RAW){
        	
        	System.out.println("Exception -->> "+ unpacker.readString());
        }
        else{
        int result = unpacker.readInt();
        System.out.println("Result-->" + result);
        }
        socket.close();
        context.term();
	}
	/**
	 * This method will send a HashMap to the server and receive a updated HashMap from the server
	 * @param methodName
	 * @param userMap
	 * @throws IOException
	 */
	public void sendHashMap(String methodName, Map<Object, Object> userMap) throws IOException{
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final MessagePack msgpack = new MessagePack();
		Packer packer = msgpack.createPacker(out);

		//send to server
		byte raw[] = msgpack.write(userMap);
		Value mapVal = msgpack.read(raw);
		Value values[] = new Value[1];
		values[0] = mapVal;
		packer.write(new CommandWrapper(methodName, values));
        this.socket.send(out.toByteArray(), 0);
		
		//receive from server
		byte[] response = this.socket.recv(0);
		ByteArrayInputStream in = new ByteArrayInputStream(response);
        Unpacker unpacker = msgpack.createUnpacker(in);
        System.out.println("Incoming class is -->>" +unpacker.getNextType());
        if(unpacker.getNextType() == ValueType.RAW){
        	System.out.println("Exception -->> "+ unpacker.readString());
        }else{
        	System.out.println("Result Received from Server is-->");
        	 Map<String, String> resultMap =  msgpack.read(raw, Templates.tMap(Templates.TString, Templates.TString));
        	 if(resultMap!=null && !resultMap.isEmpty()){
        		 for(int i=0; i<resultMap.size();i++){
        			 System.out.println("Key-->"+i+" Value-->" + resultMap.get(""+i));
        		 }
        	 }
        }
	}
	/**
	 * This method will send number array to server and retrieve the number array from server
	 * @param methodName
	 * @param numArr
	 * @throws IOException
	 */
	public void sendNumberArray(String methodName, int numArr[]) throws IOException{
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final MessagePack msgpack = new MessagePack();
		Packer packer = msgpack.createPacker(out);

		//send to server
		byte raw[] = msgpack.write(numArr);
		Value tempArr = msgpack.read(raw);
		Value values[] = new Value[1];
		values[0] = tempArr;//tempArr;
		packer.write(new CommandWrapper(methodName, values));
        this.socket.send(out.toByteArray(), 0);
		
		//receive from server
		byte[] response = this.socket.recv(0);
		ByteArrayInputStream in = new ByteArrayInputStream(response);
        Unpacker unpacker = msgpack.createUnpacker(in);
        System.out.println("Incoming class is -->>" +unpacker.getNextType());
        if(unpacker.getNextType() == ValueType.RAW){
        	System.out.println("Exception -->> "+ unpacker.readString());
        }else{
        	System.out.println("Result Received from Server is-->");
        	int resultNumArr[] =  unpacker.read(int[].class);
        	 if(resultNumArr!=null && resultNumArr.length>0){
        		 for(int i=0; i<resultNumArr.length;i++){
        			 System.out.println("Numbers from Array are-->"+resultNumArr[i]);
        		 }
        	 }
        }
	}
}