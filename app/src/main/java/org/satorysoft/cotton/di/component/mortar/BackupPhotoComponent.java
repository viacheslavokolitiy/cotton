package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.BackupPhotoView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 15.05.2015.
 */
@Component
@Singleton
public interface BackupPhotoComponent extends DaggerMortarComponent<BackupPhotoView> {
    @Override
    void inject(BackupPhotoView viewForInject);
}
