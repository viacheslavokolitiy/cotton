package org.satorysoft.cotton.di;

import android.view.View;

/**
 * Base interface for dagger components
 */
public interface DaggerComponent<T extends View> {
    void inject(T viewForInject);
}
