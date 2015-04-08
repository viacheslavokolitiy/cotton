package org.satorysoft.cotton.di.module;

import android.content.Context;
import android.content.pm.PackageManager;

import org.satorysoft.cotton.adapter.ApplicationListAdapter;
import org.satorysoft.cotton.core.model.PermissionList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
@Module
public class CoreModule {
    private Context context;

    public CoreModule(Context context){
        this.context = context;
    }

    @Provides
    public PermissionList providePermissionList(){
        return new PermissionList();
    }

    @Provides
    public PackageManager providePackageManager(){
        return context.getPackageManager();
    }

    @Provides
    public ApplicationListAdapter provideAdapter(){
        return new ApplicationListAdapter(context);
    }
}
