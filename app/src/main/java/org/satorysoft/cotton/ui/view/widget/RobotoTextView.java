package org.satorysoft.cotton.ui.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import org.satorysoft.cotton.util.TypefaceManager;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class RobotoTextView extends TextView {

    public RobotoTextView(Context context){
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(new TypefaceManager(context).createCustomTypeface("font/RobotoCondensed-Regular.ttf"));
    }
}
