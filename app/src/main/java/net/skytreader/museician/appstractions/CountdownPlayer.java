package net.skytreader.museician.appstractions;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Mostly a wrapper to MediaPlayer.
 * <p>
 * Created by chad on 6/4/17.
 */

public class CountdownPlayer {

    private Activity activity;
    private MediaPlayer mp;

    private boolean isMediaPlayerReady = true;

    private class PlayWhenPrepared implements MediaPlayer.OnPreparedListener {
        public void onPrepared(MediaPlayer mp) {
            CountdownPlayer.this.isMediaPlayerReady = true;
        }
    }

    public CountdownPlayer(Activity a, String filepath) {
        activity = a;
        mp = MediaPlayer.create(activity, Uri.parse(filepath));
        mp.setLooping(false);
        mp.setVolume(100F, 100F);
    }

    public void reset(String filepath) throws IOException {
        isMediaPlayerReady = false;
        mp.stop();
        mp.reset();
        mp.setDataSource(filepath);
        mp.setLooping(false);
        mp.setVolume(100F, 100F);
        mp.setOnPreparedListener(new PlayWhenPrepared());
        mp.prepareAsync();
    }

    public void play() {
        while (!this.isMediaPlayerReady) {
            try {
                Log.i("doInBackground", "mediaPlayer is not yet prepared.");
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Log.e("doInBackground", "InterruptedException occurred!", ie);
            }
        }
        mp.start();
    }

    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    public void togglePause() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    public void toggleStop() {
        if (mp.isPlaying()) {
            mp.stop();
        }
    }
}
