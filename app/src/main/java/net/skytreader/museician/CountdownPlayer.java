package net.skytreader.museician;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chad on 6/4/17.
 */

public class CountdownPlayer extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private String filepath;

    public CountdownPlayer(Activity a, String filepath){
        activity = a;
        this.filepath = filepath;
    }

    @Override
    protected Void doInBackground(Void... params){
        MediaPlayer mp = MediaPlayer.create(activity, (new Uri.Builder()).path
                (filepath).build());
        mp.setLooping(false);
        mp.setVolume(100F, 100F);
        mp.start();
        return null;
    }
}
