package org.satorysoft.cotton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.satorysoft.cotton.di.component.DaggerRootComponent;
import org.satorysoft.cotton.di.component.RootComponent;
import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.ui.activity.ApplicationListActivity;
import org.satorysoft.cotton.ui.activity.ApplicationScanActivity;
import org.satorysoft.cotton.util.BooleanPreference;
import org.satorysoft.cotton.util.Constants;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class MainActivity extends ActionBarActivity {
    private RootComponent rootComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        if(isFirstRun()){
            rootComponent.getBooleanPreference().set(Constants.GOOGLE_DRIVE_AUTH_SUCCESS, false);
            startActivity(new Intent(MainActivity.this, ApplicationScanActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, ApplicationListActivity.class));
            finish();
        }
    }

    private void initComponent(){
        this.rootComponent = DaggerRootComponent.builder().rootModule(new RootModule(this)).build();
    }

    private boolean isFirstRun(){
        BooleanPreference preference = rootComponent.getBooleanPreference();
        return preference.get("firstrun");
    }
}
