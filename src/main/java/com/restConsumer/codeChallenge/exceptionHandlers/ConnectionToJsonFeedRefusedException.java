package com.restConsumer.codeChallenge.exceptionHandlers;

public class ConnectionToJsonFeedRefusedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5321344995568885969L;

	public ConnectionToJsonFeedRefusedException(String message) {
		super(message);
		
	}

}
