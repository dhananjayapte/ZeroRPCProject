package main.server;

import java.io.IOException;

import main.exception.DivideByZeroException;
import main.exception.TypeMisMatchException;

import org.msgpack.type.Value;

public abstract class Operator {
	private Operator operator;
	protected String methodName;

	public void setNextOperator(Operator operator) {
		this.operator = operator;
	}

	public int operate(String methodName, Value[] argument) throws IOException,TypeMisMatchException,DivideByZeroException {
		int result=0;
		
		if(this.methodName.equalsIgnoreCase(methodName))
		{
			result = this.perform(argument);
		}else if(operator != null)
		{
			result = operator.operate(methodName, argument);
		}
		
		return result;
		
		
	}

	abstract int perform(Value[] argument) throws IOException,TypeMisMatchException,DivideByZeroException;
}
