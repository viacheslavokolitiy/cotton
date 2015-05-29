package org.satorysoft.cotton.core.event;

/**
 * Created by viacheslavokolitiy on 29.05.2015.
 */
public class NoInternetConnectionEvent {
    private final String message;

    public NoInternetConnectionEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
