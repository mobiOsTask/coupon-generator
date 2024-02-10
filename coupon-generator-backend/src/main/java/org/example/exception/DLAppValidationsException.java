package org.example.exception;



import lombok.Getter;
import lombok.Setter;
import org.example.util.RequestStatus;


@Getter
@Setter
public class DLAppValidationsException extends RuntimeException{
	private String status;
	private String code;

    public DLAppValidationsException(String statusCode, String message) {
		super(message);
        this.setStatus(String.valueOf(RequestStatus.getByStatusCode(statusCode)));
		this.code = statusCode;
	}

}
