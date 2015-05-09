package org.satorysoft.cotton.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.satorysoft.cotton.util.Constants;

/**
 * Created by viacheslavokolitiy on 09.05.2015.
 */
public class ApplicationInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String addedPackage = intent.getData().toString();
        Intent scanIntent = new Intent();
        scanIntent.putExtra(Constants.ADDED_PACKAGE_NAME, addedPackage);
        scanIntent.setAction(Constants.INTENT_SCAN_APPS);
        context.startService(scanIntent);
    }
}
