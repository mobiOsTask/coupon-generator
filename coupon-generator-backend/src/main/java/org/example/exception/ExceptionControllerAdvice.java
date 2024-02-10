package org.example.exception;


import org.example.util.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

	private final String INTERNAL_ERROR = "DL APP SYSTEM INTERNAL ERROR";
	private final Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

//
	@ExceptionHandler(DLAppValidationsException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(DLAppValidationsException ex) {
		ErrorResponse error = new ErrorResponse();
		if(ex.getCode() != null) {
			error.setResponseCode(ex.getCode());
		}else {
			error.setResponseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		}
		error.setMessage(ex.getMessage());
		error.setStatus(ex.getStatus());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleExceptions(MethodArgumentNotValidException ex){
		ErrorResponse errors = new ErrorResponse();
		errors.setResponseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		errors.setStatus(RequestStatus.BAD_REQUEST.getStatusCode());
		errors.setMessage(ex.getMessage());
		return errors;
	}

}
