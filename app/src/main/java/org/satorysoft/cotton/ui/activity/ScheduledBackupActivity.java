package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.ScheduledBackupComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;

import butterknife.ButterKnife;

/**
 * Created by viacheslavokolitiy on 14.05.2015.
 */
public class ScheduledBackupActivity extends MortarActivity<ScheduledBackupComponent> {

    @Override
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_backup);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar_scheduled_backup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCustomActionBarTitle(getString(R.string.text_scheduled_backup_toolbar));
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
