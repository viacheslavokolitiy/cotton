package org.satorysoft.cotton.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.DaggerCoreComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DrawableConverter;

/**
 * Created by viacheslavokolitiy on 09.05.2015.
 */
public class RecentApplicationScanner extends IntentService {
    private CoreComponent coreComponent;
    private PackageManager packageManager;
    private ApplicationInfo recentlyAddedApplicationInfo;

    private static final String TAG = RecentApplicationScanner.class.getName();

    public RecentApplicationScanner() {
        super(RecentApplicationScanner.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras == null) {
            throw new UnsupportedOperationException("Extras is null");
        }

        this.coreComponent = DaggerCoreComponent.builder().coreModule(new CoreModule(getApplicationContext()))
                .build();
        this.packageManager = coreComponent.getPackageManager();

        String packageName = extras.getString(Constants.ADDED_PACKAGE_NAME);

        String formattedPackageName = packageName.replace("package:", "");

        try {
            recentlyAddedApplicationInfo = packageManager.getApplicationInfo(formattedPackageName,
                    MODE_PRIVATE);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final String applicationName = (String)
                (recentlyAddedApplicationInfo != null ?
                        packageManager.getApplicationLabel(recentlyAddedApplicationInfo) : "(unknown)");
    }
}
