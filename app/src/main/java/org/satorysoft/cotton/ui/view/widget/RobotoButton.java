package org.satorysoft.cotton.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import org.satorysoft.cotton.util.TypefaceManager;

/**
 * Created by viacheslavokolitiy on 12.05.2015.
 */
public class RobotoButton extends Button {
    public RobotoButton(Context context) {
        super(context);
    }

    public RobotoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(new TypefaceManager(context).createCustomTypeface("font/RobotoCondensed-Regular.ttf"));
        setTextSize(14);
    }
}
