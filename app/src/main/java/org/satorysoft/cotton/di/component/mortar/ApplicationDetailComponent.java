package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.ApplicationDetailView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 08.04.2015.
 */
@Component
@Singleton
public interface ApplicationDetailComponent extends DaggerMortarComponent<ApplicationDetailView> {
    @Override
    void inject(ApplicationDetailView viewForInject);
}
