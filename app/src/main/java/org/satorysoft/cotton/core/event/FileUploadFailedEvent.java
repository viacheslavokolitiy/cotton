package org.satorysoft.cotton.core.event;

/**
 * Created by viacheslavokolitiy on 23.05.2015.
 */
public class FileUploadFailedEvent {
    private final String message;
    public FileUploadFailedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
