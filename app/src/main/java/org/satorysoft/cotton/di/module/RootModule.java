package org.satorysoft.cotton.di.module;

import android.content.Context;

import org.satorysoft.cotton.util.BooleanPreference;

import dagger.Module;
import dagger.Provides;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
@Module
public class RootModule {
    private Context context;

    public RootModule(Context context){
        this.context = context;
    }

    @Provides
    public BooleanPreference provideBooleanPreference(){
        return new BooleanPreference(context);
    }
}
