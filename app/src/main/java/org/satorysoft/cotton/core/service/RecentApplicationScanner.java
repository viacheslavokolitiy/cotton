package org.satorysoft.cotton.core.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;

import org.satorysoft.cotton.core.event.UpdateApplicationListEvent;
import org.satorysoft.cotton.core.model.InstalledApplication;
import org.satorysoft.cotton.core.scanner.ApplicationScanner;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.di.component.CoreComponent;
import org.satorysoft.cotton.di.component.DaggerCoreComponent;
import org.satorysoft.cotton.di.module.CoreModule;
import org.satorysoft.cotton.util.ApplicationRiskUtil;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DrawableConverter;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 09.05.2015.
 */
public class RecentApplicationScanner extends IntentService {
    private CoreComponent coreComponent;
    private PackageManager packageManager;
    private ApplicationInfo recentlyAddedApplicationInfo;
    private InstalledApplication installedApplication;

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

        DrawableConverter drawableConverter = new DrawableConverter();
        Drawable applicationIcon = recentlyAddedApplicationInfo.loadIcon(packageManager);

        byte[] imageBytes = drawableConverter.convertDrawable(applicationIcon);

        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(formattedPackageName, PackageManager.GET_PERMISSIONS);

            String[] applicationPermissions = packageInfo.requestedPermissions;

            double applicationRiskRate = (double)new ApplicationRiskUtil(coreComponent.getPermissionList().getHighRiskPermissions())
                    .evaluateApplicationRisk(applicationPermissions);

            installedApplication = new InstalledApplication();
            installedApplication.setApplicationName(applicationName);
            installedApplication.setApplicationPermissions(applicationPermissions);
            installedApplication.setApplicationRiskRate(applicationRiskRate);
            installedApplication.setPackageName(packageName);
            installedApplication.setApplicationIconBytes(imageBytes);

            updateApplicationsBase(installedApplication);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateApplicationsBase(final InstalledApplication installedApplication) {
        ContentValues values = new ContentValues();
        values.put(ScannedApplicationContract.APPLICATION_NAME, installedApplication.getApplicationName());
        values.put(ScannedApplicationContract.PACKAGE_NAME, installedApplication.getPackageName());
        values.put(ScannedApplicationContract.APPLICATION_ICON, installedApplication.getApplicationIconBytes());
        values.put(ScannedApplicationContract.APPLICATION_RISK_RATE, installedApplication.getApplicationRiskRate());
        if (installedApplication.getApplicationPermissions() == null){
            values.put(ScannedApplicationContract.APPLICATION_PERMISSIONS, TextUtils.join(ApplicationScanner.ARRAY_DIVIDER,
                    new String[]{}));
        } else {
            values.put(ScannedApplicationContract.APPLICATION_PERMISSIONS, TextUtils.join(ApplicationScanner.ARRAY_DIVIDER,
                    installedApplication.getApplicationPermissions()));
        }
        values.put(ScannedApplicationContract.SCAN_DATE, System.currentTimeMillis());

        getContentResolver().insert(ScannedApplicationContract.CONTENT_URI, values);
        EventBus.getDefault().post(new UpdateApplicationListEvent());
    }
}
