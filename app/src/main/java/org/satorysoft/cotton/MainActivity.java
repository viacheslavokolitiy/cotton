package org.satorysoft.cotton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.satorysoft.cotton.di.component.Dagger_RootComponent;
import org.satorysoft.cotton.di.component.RootComponent;
import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.ui.activity.ApplicationListActivity;
import org.satorysoft.cotton.ui.activity.ApplicationScanActivity;
import org.satorysoft.cotton.util.BooleanPreference;

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
            startActivity(new Intent(MainActivity.this, ApplicationScanActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, ApplicationListActivity.class));
        }
    }

    private void initComponent(){
        this.rootComponent = Dagger_RootComponent.builder().rootModule(new RootModule(this)).build();
    }

    private boolean isFirstRun(){
        BooleanPreference preference = rootComponent.getBooleanPreference();
        return preference.get("firstrun");
    }
}
