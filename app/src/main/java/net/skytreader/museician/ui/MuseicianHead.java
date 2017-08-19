package net.skytreader.museician.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import net.skytreader.museician.appstractions.FontCache;

/**
 * Created by chad on 7/9/17.
 */

public class MuseicianHead extends AppCompatTextView {

    public MuseicianHead(Context c, AttributeSet attrs){
        super(c, attrs);

        Typeface cinzel = FontCache.get("fonts/CinzelDecorative-Regular.ttf",
                c);
        this.setTypeface(cinzel);
    }

    @Override
    public void setTypeface(Typeface tf){
        super.setTypeface(tf);
    }
}
