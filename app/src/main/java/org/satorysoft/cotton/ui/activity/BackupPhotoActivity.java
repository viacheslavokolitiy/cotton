package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.DeclineBackupEvent;
import org.satorysoft.cotton.core.event.InitiateBackupEvent;
import org.satorysoft.cotton.di.component.mortar.BackupPhotoComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.ui.view.widget.RobotoButton;

import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
public class BackupPhotoActivity extends MortarActivity<BackupPhotoComponent> {
    private List<String> selectedPhotos;

    @Override
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void setCustomActionBarTitle(String title) {
        super.setCustomActionBarTitle(title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_data);
        EventBus.getDefault().register(this);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar_backup_data);
        setSupportActionBar(toolbar);
        setCustomActionBarTitle(getString(R.string.text_backup_data_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(InitiateBackupEvent event){
        this.selectedPhotos = event.getSelectedImages();
        final RobotoButton backupPhotoButton = ButterKnife.findById(this, R.id.btn_backup_photos);
        backupPhotoButton.setVisibility(View.VISIBLE);
    }

    public void onEvent(DeclineBackupEvent event){
        final RobotoButton backupPhotoButton = ButterKnife.findById(this, R.id.btn_backup_photos);
        backupPhotoButton.setVisibility(View.GONE);
    }
}
