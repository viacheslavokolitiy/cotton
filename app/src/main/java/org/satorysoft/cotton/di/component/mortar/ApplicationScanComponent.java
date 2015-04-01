package org.satorysoft.cotton.di.component.mortar;

import org.satorysoft.cotton.ui.view.ApplicationScanView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
@Component
@Singleton
public interface ApplicationScanComponent extends DaggerMortarComponent<ApplicationScanView> {
    @Override
    public void inject(ApplicationScanView viewForInject);
}
