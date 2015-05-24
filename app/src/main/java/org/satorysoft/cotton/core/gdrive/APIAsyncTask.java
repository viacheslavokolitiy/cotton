package org.satorysoft.cotton.core.gdrive;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.satorysoft.cotton.di.component.DaggerGoogleDriveComponent;
import org.satorysoft.cotton.di.module.GoogleDriveModule;

import java.util.concurrent.CountDownLatch;

/**
 * Created by viacheslavokolitiy on 23.05.2015.
 */
public abstract class APIAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result> {

    private GoogleApiClient mApiClient;

    public APIAsyncTask(Context context){
        this.mApiClient = DaggerGoogleDriveComponent
                            .builder()
                            .googleDriveModule(new GoogleDriveModule(context))
                            .build()
                            .getGoogleAPIClient();
    }

    @Override
    protected Result doInBackground(Params... params) {
        final CountDownLatch latch = new CountDownLatch(1);
        mApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                latch.countDown();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });

        mApiClient.registerConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                latch.countDown();
            }
        });

        mApiClient.connect();

        try {
            latch.await();
        } catch (InterruptedException e) {
            return null;
        }

        if (!mApiClient.isConnected()) {
            return null;
        }
        try {
            return doInBackgroundConnected(params);
        } finally {
            mApiClient.disconnect();
        }
    }

    protected abstract Result doInBackgroundConnected(Params[] params);

    protected GoogleApiClient getGoogleApiClient() {
        return mApiClient;
    }
}
