package net.skytreader.museician.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.util.AttributeSet;
import android.widget.TextView;

import net.skytreader.museician.appstractions.FontCache;

/**
 * Created by chad on 7/9/17.
 */

public class MuseicianHead extends TextView {
    private String text;

    public MuseicianHead(Context c, AttributeSet attrs){
        super(c, attrs);

        text = attrs.getAttributeValue("http://schemas.android" +
                ".com/apk/res/net.skytreader.museician", "text");

        Typeface cinzel = FontCache.get("fonts/CinzelDecorative-Regular.ttf",
                c);
        this.setTypeface(cinzel);
    }

    @Override
    public void setTypeface(Typeface tf){
        super.setTypeface(tf);
    }
}
