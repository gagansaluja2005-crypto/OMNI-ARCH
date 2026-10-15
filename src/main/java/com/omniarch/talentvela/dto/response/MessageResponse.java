package com.omniarch.talentvela.dto.response;

/**
 * Standard message response for informational / state-mutation endpoints.
 */
public class MessageResponse {

    private boolean success;
    private String message;

    public MessageResponse() {
    }

    public MessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static MessageResponse success(String message) {
        return new MessageResponse(true, message);
    }

    public static MessageResponse failure(String message) {
        return new MessageResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
