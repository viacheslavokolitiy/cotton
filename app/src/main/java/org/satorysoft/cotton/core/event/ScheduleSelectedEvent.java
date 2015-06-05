package org.satorysoft.cotton.core.event;

import java.util.Calendar;

/**
 * Created by viacheslavokolitiy on 05.06.2015.
 */
public class ScheduleSelectedEvent {
    private final Calendar calendar;

    public ScheduleSelectedEvent(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
