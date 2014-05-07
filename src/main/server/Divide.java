package main.server;

import java.io.IOException;

import main.exception.DivideByZeroException;
import main.exception.TypeMisMatchException;
import main.utils.ConstantUtils;

import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

public class Divide extends Operator{
	
	public Divide()
	{
	this.methodName = "divide";
	}
	
	private int divide(int a, int b)
	{
		return a/b;
	}

	@Override
	int perform(Value[] argument) throws DivideByZeroException,TypeMisMatchException {
		try {
		
			int a = new Converter(argument[0]).readInt();
			int b = new Converter(argument[1]).readInt();
			
			if (b == 0){
				throw new DivideByZeroException(ConstantUtils.DIVIDE_BY_ZERO);
			}
			return divide(a,b);
		} 
		catch (IOException e) {
			// TODO: handle exception
			throw new TypeMisMatchException(ConstantUtils.TYPE_MISMATCH);
		}
		
	}

}
