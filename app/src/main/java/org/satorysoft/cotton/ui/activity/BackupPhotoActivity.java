package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.BackupPhotoComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;

import butterknife.ButterKnife;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
public class BackupPhotoActivity extends MortarActivity<BackupPhotoComponent> {
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
}
