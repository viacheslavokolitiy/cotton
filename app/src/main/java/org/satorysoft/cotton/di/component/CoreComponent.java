package org.satorysoft.cotton.di.component;

import android.content.pm.PackageManager;

import org.satorysoft.cotton.adapter.ApplicationListAdapter;
import org.satorysoft.cotton.adapter.PermissionListAdapter;
import org.satorysoft.cotton.core.model.PermissionList;
import org.satorysoft.cotton.di.module.CoreModule;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
@Component(modules = {CoreModule.class})
public interface CoreComponent {
    PermissionList getPermissionList();
    PackageManager getPackageManager();
    ApplicationListAdapter getAdapter();
    PermissionListAdapter getPermissionsAdapter();
}
