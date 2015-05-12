package org.satorysoft.cotton.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by viacheslavokolitiy on 12.05.2015.
 */
public class TypefaceManager {
    private Typeface mTypeface;
    private final Context context;

    public TypefaceManager(Context context){
        this.context = context;
    }

    public Typeface createCustomTypeface(String typefaceName){
        mTypeface = Typeface.createFromAsset(context.getAssets(), typefaceName);

        return mTypeface;
    }
}
