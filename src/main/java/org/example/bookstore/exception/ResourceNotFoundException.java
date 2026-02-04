package org.example.bookstore.exception;

import org.example.bookstore.enums.MessageException;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String message;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(MessageException messageException) {
		this.message = messageException.getMessage();
	}




}
