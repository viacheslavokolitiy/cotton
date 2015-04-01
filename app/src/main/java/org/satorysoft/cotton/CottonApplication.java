package org.satorysoft.cotton;

import android.app.Application;

import mortar.MortarScope;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class CottonApplication extends Application {
    private MortarScope rootScope;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Object getSystemService(String name) {
        if (rootScope == null) rootScope = MortarScope.buildRootScope().build("Root");

        return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
    }
}
