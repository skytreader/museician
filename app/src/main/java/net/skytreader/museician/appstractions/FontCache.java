package net.skytreader.museician.appstractions;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * See https://stackoverflow.com/questions/16901930/memory-leaks-with-custom
 * -font-for-set-custom-font/16902532#16902532.
 *
 * Created by chad on 7/2/17.
 */

public class FontCache {
    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                Log.e("FontCache", "Unable to load font file due to " +
                        "Exception", e);
                return null;
            }
            Log.i("FontCache", "found font file " + tf);
            fontCache.put(name, tf);
        }
        return tf;
    }
}
