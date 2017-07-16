package net.skytreader.museician.appstractions;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Mostly a wrapper to MediaPlayer.
 * <p>
 * Created by chad on 6/4/17.
 */

public class CountdownPlayer {

    private Activity activity;
    private MediaPlayer mp;
    private String lastDisplaySnapshot = "00:00";

    private boolean isMediaPlayerReady = true;

    private class PlayWhenPrepared implements MediaPlayer.OnPreparedListener {
        public void onPrepared(MediaPlayer mp) {
            CountdownPlayer.this.isMediaPlayerReady = true;
            Log.i("PlayWhenPrepared", "media player should now be ready.");
        }
    }

    public CountdownPlayer(Activity a, String filepath) {
        activity = a;
        mp = MediaPlayer.create(activity, Uri.parse(filepath));
        mp.setLooping(false);
        mp.setVolume(100F, 100F);
    }

    /**
     * Use this when loading a new song into the player.
     *
     * @param filepath
     * @throws IOException
     */
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

    public String getTimedownDisplay(){
        if(mp.isPlaying()) {
            long duration = mp.getDuration();
            long secsDuration = TimeUnit.MILLISECONDS.toSeconds(duration);

            long progress = mp.getCurrentPosition();
            long secsProgress = TimeUnit.MILLISECONDS.toSeconds(progress);
            long secsLeft = secsDuration - secsProgress;
            long minDisplay = secsLeft / 60;
            long secsDisplay = secsLeft % 60;
            secsDisplay = secsDisplay < 0 ? 0 : secsDisplay;

            lastDisplaySnapshot = String.format("%02d:%02d", minDisplay,
                    secsDisplay);
        }

        return lastDisplaySnapshot;
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
            mp.prepareAsync();
            isMediaPlayerReady = false;
            // FIXME Maybe don't create a new PlayWhenPrepared object everytime.
            mp.setOnPreparedListener(new PlayWhenPrepared());
        }
    }
}
