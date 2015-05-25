package org.satorysoft.cotton.ui;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.ActionModeDestroyedEvent;
import org.satorysoft.cotton.core.event.BackupPhotoEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 25.05.2015.
 */
public class SelectPhotoCallback implements ActionMode.Callback {
    private final Context context;

    public SelectPhotoCallback(Context context){
        this.context = context;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_upload_to_drive:
               EventBus.getDefault().post(new BackupPhotoEvent(context));
               break;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        EventBus.getDefault().post(new ActionModeDestroyedEvent());
    }
}
