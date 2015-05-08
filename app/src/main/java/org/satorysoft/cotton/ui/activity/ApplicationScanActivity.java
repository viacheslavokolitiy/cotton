package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.CompletedScanEvent;
import org.satorysoft.cotton.di.component.mortar.ApplicationScanComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.util.DaggerService;

import de.greenrobot.event.EventBus;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static org.satorysoft.cotton.util.DaggerService.createComponent;

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
        getSupportActionBar().hide();
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
