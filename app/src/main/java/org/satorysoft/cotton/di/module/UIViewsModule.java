package org.satorysoft.cotton.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by viacheslavokolitiy on 10.04.2015.
 */
@Module
public class UIViewsModule {
    private final Context context;

    public UIViewsModule(Context context){
        this.context = context;
    }

    @Provides
    public MaterialDialog provideMaterialDialog(){
        return new MaterialDialog(context);
    }
}
