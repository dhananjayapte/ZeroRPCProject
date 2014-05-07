package main.server;

//  Hello World server in Java
//  Binds REP socket to tcp://*:port
//  Expects "Hello" from client, replies with "World"

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import main.command.CommandWrapper;
import main.exception.DivideByZeroException;
import main.exception.TypeMisMatchException;
import main.library.Book;
import main.library.BookRepository;
import main.library.BookRepositoryInterface;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;
import org.zeromq.ZMQ;

public class Server implements Runnable {

	ZMQ.Context context = null;
	ZMQ.Socket socket = null;

	public void bind(String address) {
		this.context = ZMQ.context(1);
		this.socket = context.socket(ZMQ.REP);
		this.socket.bind(address);
	}

	public String executeMethod(CommandWrapper command) {
		String methodName = command.getMethodName();
		String reply = null;
		if (methodName.equalsIgnoreCase("sayhellotoserver")) {
			reply = sayHelloToServer();
		}
		return reply;
	}

	public int performOperation(CommandWrapper command) throws IOException, TypeMisMatchException, DivideByZeroException {
		String methodName = command.getMethodName();
		Value[] argument = command.getArgs();
		
		Operator operator = getChainOfOperators();
		int result = operator.operate(methodName, argument);

		return result;
	}
	
	public Map<String, String> printMap(CommandWrapper command) throws IOException{
		//System.out.println("Inside printMap function");
		Value[] argument = command.getArgs();
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = new Converter(argument[0]).read(
					Templates.tMap(Templates.TString, Templates.TString));
		for(int i=0; i<resultMap.size();i++){
			resultMap.put(""+i, resultMap.get(""+i)+"::FromServer");
			//System.out.println("Key-->"+i+" ::Value-->"+resultMap.get(""+i));
		}
		return resultMap;
	}
	
	public Book saveBook(CommandWrapper command){
		System.out.println("Save Book Method on ZeroRPC Server Called");
		MessagePack msgpack = new MessagePack();
		byte[] argument = command.getBytes();
		Value values[] = command.getArgs();
		MessagePack packer = new MessagePack();
		Book savedBook = null;
		try {
			Book bookObj = new Converter(values[0]).read(Book.class);
			//MyMessage dst = msgpack.read(bytes, MyMessage.class);
			System.out.println("BookObj Using values:"+bookObj.getTitle());
			//packer.register(Book.class);
			Book dst = msgpack.read(argument, Book.class);
			/*Book book1 =  packer.createUnpacker(
					new ByteArrayInputStream(argument)).read(
					new Book());*/
			System.out.println("using bytes Isbn:"+dst.getIsbn()+"|| Title"+dst.getTitle());
			
			BookRepositoryInterface bookRepository = new BookRepository(new ConcurrentHashMap<Long, Book>());
			savedBook = bookRepository.saveBook(dst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return savedBook;
	}
	
	/**
	 * This method will retrieve the received array and multiply the number by 2 and return the modified array
	 * @param command
	 * @return
	 * @throws IOException
	 */
	private int[] printNumArr(CommandWrapper command) throws IOException {
		Value[] argument = command.getArgs();
		MessagePack msgpack = new MessagePack();
		byte raw[] = msgpack.write(argument);
		ByteArrayInputStream in = new ByteArrayInputStream(raw);
        Unpacker unpacker = msgpack.createUnpacker(in);
		
        int[] resultArr = new int[4];
		resultArr = unpacker.read(int[].class);
		for(int i=0; i<resultArr.length;i++){
			//System.out.println("old value is-->"+resultArr[i]);
			resultArr[i] = resultArr[i] * 2;
			//System.out.println("new value is-->"+resultArr[i]);
		}
		
		return resultArr;
	}

	public Operator getChainOfOperators() {
		Operator add = new Add();
		Operator subtract = new Subtract();
		Operator multiply = new Multiply();
		Operator divide = new Divide();

		add.setNextOperator(subtract);
		subtract.setNextOperator(multiply);
		multiply.setNextOperator(divide);

		return add;
	}

	public String sayHelloToServer() {
		return "Hello World";
	}

	@Override
	public void run() {
		System.out.println("Server Started");
		while (true) {
			final MessagePack packer = new MessagePack();
			final ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				// Wait for next request from the client
				byte[] request = socket.recv(0);
				// extract methodname from request
				final CommandWrapper command = packer.createUnpacker(
						new ByteArrayInputStream(request)).read(
						new CommandWrapper());
				if("sendHashMap".equals(command.getMethodName())){
					System.out.println("sendHashMap() method called...");
					packer.write(out, printMap(command));	
				}else if("sendNumberArray".equals(command.getMethodName())){
					System.out.println("sendNumberArray() method called...");
					packer.write(out, printNumArr(command));	
				}else if("saveBook".equals(command.getMethodName())){
					packer.write(out, saveBook(command));
				}else{
					System.out.println(command.getMethodName()+"() method called...");
					packer.write(out, performOperation(command));
				}
				this.socket.send(out.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error at server!!!");
				try {
					packer.write(out, e.getMessage());
					this.socket.send(out.toByteArray());
				} catch (IOException e1) {
					e1.printStackTrace();
					//break;
				}
				//break;
			}

		}
		//socket.close();
		//context.term();
	}

}