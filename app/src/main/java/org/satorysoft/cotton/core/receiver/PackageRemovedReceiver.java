package org.satorysoft.cotton.core.receiver;

import android.content.Context;
import android.content.Intent;

import org.satorysoft.cotton.core.receiver.base.BaseApplicationReceiver;
import org.satorysoft.cotton.util.Constants;

/**
 * Created by viacheslavokolitiy on 10.05.2015.
 */
public class PackageRemovedReceiver extends BaseApplicationReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent packageRemoveIntent = createApplicationIntent(intent, Constants.INTENT_REMOVE_APP);
        context.startService(packageRemoveIntent);
    }
}
