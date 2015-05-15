package org.satorysoft.cotton.ui.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.event.ShowBackupScreenEvent;
import org.satorysoft.cotton.di.component.DaggerRootComponent;
import org.satorysoft.cotton.di.component.RootComponent;
import org.satorysoft.cotton.di.component.mortar.GoogleDriveAuthComponent;
import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.ui.activity.base.MortarActivity;
import org.satorysoft.cotton.util.Constants;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 14.05.2015.
 */
public class GoogleDriveAuthActivity extends MortarActivity<GoogleDriveAuthComponent>
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1500;
    private GoogleApiClient mGoogleAPIClient;
    private RootComponent rootComponent;

    @Override
    public String getScopeName() {
        return getClass().getName();
    }

    @Override
    public void setCustomActionBarTitle(String title) {
        super.setCustomActionBarTitle(title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive_auth);
        EventBus.getDefault().register(this);
        this.rootComponent = DaggerRootComponent.builder().rootModule(new RootModule(this)).build();

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar_backup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setCustomActionBarTitle(getString(R.string.app_name));

        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        ButterKnife.findById(this, R.id.btn_google_drive_login)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mGoogleAPIClient.connect();
                    }
                });
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
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }

    @Override
    public void onConnected(Bundle bundle) {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle("Cotton").build();
        Drive.DriveApi.getRootFolder(mGoogleAPIClient).createFolder(
                mGoogleAPIClient, changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
            @Override
            public void onResult(DriveFolder.DriveFolderResult driveFolderResult) {
                if (!driveFolderResult.getStatus().isSuccess()) {
                    return;
                }

                EventBus.getDefault().post(new ShowBackupScreenEvent());
            }
        });

    }

    public void onEvent(ShowBackupScreenEvent event){
        rootComponent.getBooleanPreference().set(Constants.GOOGLE_DRIVE_AUTH_SUCCESS);
        ButterKnife.findById(this, R.id.btn_google_drive_login).setVisibility(View.GONE);
        setCustomActionBarTitle(getString(R.string.text_backup_data_toolbar));
        startActivity(new Intent(GoogleDriveAuthActivity.this, BackupActivity.class));
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            mGoogleAPIClient.connect();
        }
    }
}
