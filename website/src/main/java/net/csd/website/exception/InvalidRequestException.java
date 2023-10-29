package net.csd.website.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidRequestException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public InvalidRequestException(String message) {
		super(message);
	}
	
}
