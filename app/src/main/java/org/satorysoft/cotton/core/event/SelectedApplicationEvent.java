package org.satorysoft.cotton.core.event;

import android.content.Intent;

/**
 * Created by viacheslavokolitiy on 19.05.2015.
 */
public class SelectedApplicationEvent {
    private final Intent intent;

    public SelectedApplicationEvent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }
}
