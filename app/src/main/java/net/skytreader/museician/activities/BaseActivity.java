package net.skytreader.museician.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
        setContentView(R.layout.app_header);
        super.onCreate(savedInstance);
        TextView appHeader = (TextView) findViewById(R.id.appNameHeader);
        if(appHeader != null) {
            Log.i("branding", "appHeader is not null");
            Typeface cinzel = FontCache.get
                    ("fonts/CinzelDecorative-Regular.ttf",
                    this);
            appHeader.setTypeface(cinzel);
        }
    }

}
