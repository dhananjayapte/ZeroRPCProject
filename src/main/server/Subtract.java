package main.server;

import java.io.IOException;

import main.exception.TypeMisMatchException;
import main.utils.ConstantUtils;

import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

public class Subtract extends Operator{
	
	public Subtract()
	{
	this.methodName = "subtract";
	}
	
	private int subtract(int a, int b)
	{
		return a-b;
	}

	@Override
	int perform(Value[] argument) throws IOException ,TypeMisMatchException {
		try{
		int a = new Converter(argument[0]).readInt();
		int b = new Converter(argument[1]).readInt();
		
		return subtract(a, b);
		}
		catch(IOException e){
			throw new TypeMisMatchException(ConstantUtils.TYPE_MISMATCH);
			
		}
	}

}
