package org.satorysoft.cotton.core.event;

import android.content.Context;

/**
 * Created by viacheslavokolitiy on 25.05.2015.
 */
public class BackupPhotoEvent {
    private final Context context;

    public BackupPhotoEvent(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
