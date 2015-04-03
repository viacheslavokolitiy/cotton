package org.satorysoft.cotton.di.mortar;

import android.content.Context;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.satorysoft.cotton.core.scanner.ApplicationScanner;
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

        public void launchApplicationScan(Context context, ArcProgress progress) {
            new ApplicationScanner(context, progress).execute();
        }
    }
}
