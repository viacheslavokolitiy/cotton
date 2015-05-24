package org.satorysoft.cotton.di.component;

import com.google.android.gms.common.api.GoogleApiClient;

import org.satorysoft.cotton.di.module.GoogleDriveModule;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 23.05.2015.
 */
@Component(modules = {GoogleDriveModule.class})
public interface GoogleDriveComponent {
    GoogleApiClient getGoogleAPIClient();
}
