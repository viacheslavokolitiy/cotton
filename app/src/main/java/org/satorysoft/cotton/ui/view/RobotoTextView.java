package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class RobotoTextView extends TextView {
    private Typeface mCustomFontFace;

    public RobotoTextView(Context context){
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface("font/RobotoCondensed-Regular.ttf");
    }

    public void setCustomTypeface(String typefaceName) {
        assert getContext() != null;
        mCustomFontFace = Typeface.createFromAsset(getContext().getAssets(), typefaceName);
        setTypeface(mCustomFontFace);
    }
}
