package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.BackupView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
@Component
@Singleton
public interface BackupComponent extends DaggerMortarComponent<BackupView> {
    @Override
    void inject(BackupView viewForInject);
}
