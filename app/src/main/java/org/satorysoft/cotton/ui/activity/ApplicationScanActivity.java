package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.CompletedScanEvent;
import org.satorysoft.cotton.di.component.mortar.ApplicationScanComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class ApplicationScanActivity extends MortarActivity<ApplicationScanComponent> {
    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_scan);
        EventBus.getDefault().register(this);
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
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void setCustomActionBarTitle(String title) {
        //do nothing now
    }

    public void onEvent(CompletedScanEvent event){
        startActivity(new Intent(ApplicationScanActivity.this, ApplicationListActivity.class));
    }
}
