package org.satorysoft.cotton.di.component;

import org.satorysoft.cotton.di.module.RootModule;
import org.satorysoft.cotton.util.BooleanPreference;

import dagger.Component;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
@Component(modules = {RootModule.class})
public interface RootComponent {
    BooleanPreference getBooleanPreference();
}
