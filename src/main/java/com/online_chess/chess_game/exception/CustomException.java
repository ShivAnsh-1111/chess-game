package com.online_chess.chess_game.exception;


public class CustomException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
        super(message); // This sets the exception message
    }
}
