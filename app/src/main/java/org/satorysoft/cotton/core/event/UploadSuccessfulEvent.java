package org.satorysoft.cotton.core.event;

/**
 * Created by viacheslavokolitiy on 25.05.2015.
 */
public class UploadSuccessfulEvent {
    private String message;

    public UploadSuccessfulEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
