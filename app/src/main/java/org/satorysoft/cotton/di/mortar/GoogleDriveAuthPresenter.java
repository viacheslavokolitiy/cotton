package org.satorysoft.cotton.di.mortar;

import org.satorysoft.cotton.ui.view.GoogleDriveAuthView;

import javax.inject.Inject;
import javax.inject.Singleton;

import mortar.ViewPresenter;

/**
 * Created by viacheslavokolitiy on 14.05.2015.
 */
@Singleton
public class GoogleDriveAuthPresenter extends ViewPresenter<GoogleDriveAuthView> {

    @Inject
    public GoogleDriveAuthPresenter(){

    }
}
