package org.satorysoft.cotton.core.event;

import android.content.Context;

import org.satorysoft.cotton.ui.activity.ScheduledBackupActivity;

/**
 * Created by viacheslavokolitiy on 05.06.2015.
 */
public class SaveScheduleEvent {
    private Context context;
    public SaveScheduleEvent(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
