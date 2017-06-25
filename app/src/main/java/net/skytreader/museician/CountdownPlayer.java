package net.skytreader.museician;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Mostly a wrapper to MediaPlayer.
 *
 * Created by chad on 6/4/17.
 */

public class CountdownPlayer extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private MediaPlayer mp;

    public CountdownPlayer(Activity a, String filepath){
        activity = a;
        mp = MediaPlayer.create(activity, Uri.parse(filepath));
        mp.setLooping(false);
        mp.setVolume(100F, 100F);
    }

    @Override
    protected Void doInBackground(Void... params){
        mp.start();
        return null;
    }

    public void reset(String filepath) throws IOException{
        mp.reset();
        mp.setDataSource(filepath);
        mp.prepare();
    }

    public void play(){
        mp.start();
    }

    public MediaPlayer getMediaPlayer(){
        return mp;
    }

    public void togglePause(){
        if(mp.isPlaying()){
            mp.pause();
        }
    }

    public void toggleStop(){
        if(mp.isPlaying()){
            mp.stop();
        }
    }
}
