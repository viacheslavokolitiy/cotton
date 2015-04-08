package org.satorysoft.cotton.ui.activity.base;

import android.support.v7.app.ActionBarActivity;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
public abstract class MortarActivity extends ActionBarActivity {
    public abstract String getScopeName();
    public abstract void setCustomActionBarTitle(String title);
}
