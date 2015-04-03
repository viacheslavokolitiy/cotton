package org.satorysoft.cotton.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.di.component.mortar.ApplicationListComponent;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortar.dagger2support.DaggerService;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static mortar.dagger2support.DaggerService.createComponent;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
public class ApplicationListActivity extends ActionBarActivity {
    @Override
    public Object getSystemService(String name) {
        MortarScope activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, createComponent(ApplicationListComponent.class))
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }

    private String getScopeName() {
        return getClass().getName();
    }
}
