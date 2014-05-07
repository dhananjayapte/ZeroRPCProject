package main.server;

import java.io.IOException;

import main.exception.TypeMisMatchException;
import main.utils.ConstantUtils;

import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

public class Add extends Operator {

	public Add()
	{
	this.methodName = "add";
	}
	
	int add(int a, int b)
	{
		return a+b;
	}
	
	@Override
	int perform(Value[] argument) throws IOException,TypeMisMatchException {
		int result=0;
		try{
			for(int i=0; i<argument.length;i++)
		
		{
			int a = new Converter(argument[i]).readInt();
			result = add(result,a);
		}
		return result;
	}
		catch(IOException e){
			throw new TypeMisMatchException(ConstantUtils.TYPE_MISMATCH);
		}
	}
}
