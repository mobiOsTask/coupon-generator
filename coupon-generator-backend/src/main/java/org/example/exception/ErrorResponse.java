package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private String responseCode;
	private String status;
	private String message;
}
