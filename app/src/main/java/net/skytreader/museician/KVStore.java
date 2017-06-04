package net.skytreader.museician;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Convenience class to work mostly with SharedPreferences. At this point
 * this is little more than syntactic sugar to make SharedPreferences work
 * like a hash map. You don't need to remember to commit after making a
 * change anymore!
 *
 * Created by chad on 6/4/17.
 */

public class KVStore {

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public KVStore(Context appContext, String namespace){
        sp = appContext.getSharedPreferences(namespace, Context.MODE_PRIVATE);
    }
}
