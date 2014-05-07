package main.exception;

import main.utils.ConstantUtils;

public class TypeMisMatchException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeMisMatchException(String message){
		super(message);
	}
}
