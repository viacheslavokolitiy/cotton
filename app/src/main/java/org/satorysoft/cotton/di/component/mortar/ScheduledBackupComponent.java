package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.ScheduledBackupView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 02.06.2015.
 */
@Singleton
@Component
public interface ScheduledBackupComponent extends DaggerMortarComponent<ScheduledBackupView> {
    @Override
    void inject(ScheduledBackupView viewForInject);
}
