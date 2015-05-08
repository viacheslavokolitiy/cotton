package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.PopulateCardViewEvent;
import org.satorysoft.cotton.core.model.SelectedApplication;
import org.satorysoft.cotton.di.component.mortar.ApplicationDetailComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.ui.view.RobotoTextView;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DaggerService;

import de.greenrobot.event.EventBus;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static org.satorysoft.cotton.util.DaggerService.createComponent;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
public class ApplicationDetailActivity extends MortarActivity<ApplicationDetailComponent> {
    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void setCustomActionBarTitle(String title) {
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_action_bar_title, null);
        ((RobotoTextView)v.findViewById(R.id.text_custom_action_bar_title)).setText(title);
        getSupportActionBar().setCustomView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_detail);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        SelectedApplication selectedApplication = (SelectedApplication) intent.getSerializableExtra(Constants.SCANNED_APPLICATION);
        setCustomActionBarTitle(selectedApplication.getTitle());
        EventBus.getDefault().post(new PopulateCardViewEvent(selectedApplication));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
