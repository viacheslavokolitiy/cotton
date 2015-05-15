package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.GoogleDriveAuthView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 14.05.2015.
 */
@Component
@Singleton
public interface GoogleDriveAuthComponent extends DaggerMortarComponent<GoogleDriveAuthView> {
    @Override
    void inject(GoogleDriveAuthView viewForInject);
}
