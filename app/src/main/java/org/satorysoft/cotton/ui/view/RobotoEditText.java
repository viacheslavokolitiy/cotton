package org.satorysoft.cotton.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by viacheslavokolitiy on 10.04.2015.
 */
public class RobotoEditText extends EditText {
    private Typeface mCustomFontFace;

    public RobotoEditText(Context context){
        super(context);
    }

    public RobotoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface("font/RobotoCondensed-Regular.ttf");
    }

    public void setCustomTypeface(String typefaceName) {
        assert getContext() != null;
        mCustomFontFace = Typeface.createFromAsset(getContext().getAssets(), typefaceName);
        setTypeface(mCustomFontFace);
    }
}
