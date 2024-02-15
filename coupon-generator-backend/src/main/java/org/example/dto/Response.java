package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Response {
    private String responseCode;
    private String status;
    private String message;

    @JsonIgnore
    private String statusCode;
    @JsonIgnore
    private String statusDesc;
}
