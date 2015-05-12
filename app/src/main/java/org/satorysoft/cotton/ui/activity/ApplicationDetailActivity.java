package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.ApplicationRemovedEvent;
import org.satorysoft.cotton.core.event.PopulateCardViewEvent;
import org.satorysoft.cotton.core.model.ScannedApplication;
import org.satorysoft.cotton.core.model.SelectedApplication;
import org.satorysoft.cotton.core.receiver.PackageRemovedReceiver;
import org.satorysoft.cotton.db.contract.ScannedApplicationContract;
import org.satorysoft.cotton.di.component.mortar.ApplicationDetailComponent;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.ui.view.widget.RobotoButton;
import org.satorysoft.cotton.ui.view.widget.RobotoTextView;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.DpUtil;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

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
        EventBus.getDefault().register(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        SelectedApplication selectedApplication = (SelectedApplication) intent.getSerializableExtra(Constants.SCANNED_APPLICATION);
        setCustomActionBarTitle(selectedApplication.getTitle());
        EventBus.getDefault().post(new PopulateCardViewEvent(selectedApplication));
        Cursor cursor = getContentResolver().query(ScannedApplicationContract.CONTENT_URI, null,
                ScannedApplicationContract.APPLICATION_NAME + "=?", new String[]{selectedApplication.getTitle()}, null);
        if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
            do {
                double risk = cursor.getDouble(cursor.getColumnIndex(ScannedApplicationContract.APPLICATION_RISK_RATE));
                if(risk < Constants.APPLICATION_MEDIUM_RISK){
                    RobotoButton robotoButton = ButterKnife.findById(this, R.id.btn_trust_application);
                    robotoButton.setVisibility(View.GONE);

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT
                    );

                    layoutParams.setMargins(0, DpUtil.dpToPx(60), 0, 0);
                    ButterKnife.findById(this, R.id.btn_delete_application).setLayoutParams(layoutParams);
                } else {
                    ButterKnife.findById(this, R.id.btn_trust_application).setVisibility(View.VISIBLE);
                }
            } while (cursor.moveToNext());
        }

        registerReceiver(new PackageRemovedReceiver(), new IntentFilter(Constants.INTENT_REMOVE_APP));
    }

    public void onEvent(ApplicationRemovedEvent event){
        finish();
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
