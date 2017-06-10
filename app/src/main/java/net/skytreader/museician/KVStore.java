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

    public KVStore(SharedPreferences sp){
        this.sp = sp;
        spEditor = sp.edit();
    }

    public String get(String key, String dflt){
        return sp.getString(key, dflt);
    }

    public void set(String key, String newVal){
        spEditor.putString(key, newVal);
        spEditor.commit();
    }
}
