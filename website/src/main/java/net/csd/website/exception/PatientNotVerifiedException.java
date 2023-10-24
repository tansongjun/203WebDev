package net.csd.website.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PatientNotVerifiedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PatientNotVerifiedException(String message) {
		super(message);
	}
	
}
