package org.satorysoft.cotton.di.component.mortar;

import android.view.View;

/**
 * Base interface for dagger components for mortar
 */
public interface DaggerMortarComponent<T extends View> {
    void inject(T viewForInject);
}
