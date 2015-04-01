package org.satorysoft.cotton.di.mortar;

import org.satorysoft.cotton.ui.view.ApplicationScanView;

import javax.inject.Inject;
import javax.inject.Singleton;

import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class ApplicationScan {

    @Singleton
    public static class ApplicationScanPresenter extends ViewPresenter<ApplicationScanView> {
        @Inject
        public ApplicationScanPresenter(){

        }
    }
}
