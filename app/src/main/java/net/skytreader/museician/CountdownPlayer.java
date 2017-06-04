package net.skytreader.museician;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chad on 6/4/17.
 */

public class CountdownPlayer extends AsyncTask<Void, Void, Void> {

    private Activity activity;

    public CountdownPlayer(Activity a){
        activity = a;
    }

    @Override
    protected Void doInBackground(Void... params){
        return null;
    }
}
