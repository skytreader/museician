package net.skytreader.museician.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.skytreader.museician.R;
import net.skytreader.museician.appstractions.FontCache;

/**
 * Created by chad on 7/2/17.
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.app_header);
        LinearLayout ll = (LinearLayout) findViewById(R.id.appHeaderBar);
        TextView appHeader = (TextView) ll.findViewById(R.id.appNameHeader);
        if(appHeader != null) {
            Typeface cinzel = FontCache.get("fonts/CinzelDecorative-Regular.ttf",
                    this);
            appHeader.setTypeface(cinzel);
        }
    }

}
