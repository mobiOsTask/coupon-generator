package org.example.util;

public enum RequestStatus {

    SUCCESS("200", "SUCCESS"),
    NOT_FOUND("404", "Not-Found"),
    BAD_REQUEST("400", "Bad-Request"),
    UNPROCESSABLE_ENTITY("422", "Unprocessable-Entity"),
    FAILURE("500", "failure");

    private final String statusCode;
    private final String statusMessage;

    RequestStatus(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public static RequestStatus getByStatusCode(String statusCode) {
        for (RequestStatus requestStatus : values()) {
            if (requestStatus.getStatusCode() == statusCode) {
                return requestStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + statusCode);
    }
}
