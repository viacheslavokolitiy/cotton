package org.satorysoft.cotton.di.component;

import org.satorysoft.cotton.di.module.UIViewsModule;

import dagger.Component;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by viacheslavokolitiy on 10.04.2015.
 */
@Component(modules = {UIViewsModule.class})
public interface UIViewsComponent {
    MaterialDialog getMaterialDialog();
}
