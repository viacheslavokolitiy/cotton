package org.satorysoft.cotton.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import org.satorysoft.cotton.util.TypefaceManager;

/**
 * Created by viacheslavokolitiy on 10.04.2015.
 */
public class RobotoEditText extends EditText {
    public RobotoEditText(Context context){
        super(context);
    }

    public RobotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(new TypefaceManager(context).createCustomTypeface("font/RobotoCondensed-Regular.ttf"));
    }
}
