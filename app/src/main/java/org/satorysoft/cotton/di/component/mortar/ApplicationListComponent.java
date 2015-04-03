package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.ApplicationListView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 03.04.2015.
 */
@Component
@Singleton
public interface ApplicationListComponent extends DaggerMortarComponent<ApplicationListView> {
    @Override
    void inject(ApplicationListView viewForInject);
}
