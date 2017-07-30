package net.skytreader.museician.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import net.skytreader.museician.appstractions.CountdownPlayer;
import net.skytreader.museician.appstractions.FontCache;
import net.skytreader.museician.appstractions.KVStore;
import net.skytreader.museician.appstractions.LRUPriorityQueue;
import net.skytreader.museician.appstractions.PermissionsRequest;
import net.skytreader.museician.R;
import net.skytreader.museician.appstractions.Utils;

import java.io.IOException;

public class CountdownPlayActivity extends Activity {

    private CountdownPlayer countdownPlayer;

    private TextView statusUpdateElement;
    private SeekBar seekBar;
    private ImageButton playBtn;
    private ImageButton pauseBtn;
    private ImageButton stopBtn;
    private ImageButton backBtn;
    private ImageButton forwardBtn;

    private int countTime;
    private int displayCount;
    private long intervalMillis;
    private KVStore appKVStore;
    private LRUPriorityQueue recentFiles;
    private boolean isCountdownOngoing = false;

    private Handler seekUpdateHandler = new Handler();
    private Runnable uiUpdateRunner = new Runnable(){
        @Override
        public void run() {
            seekBar.setProgress(countdownPlayer.getMediaPlayer().getCurrentPosition());
            seekUpdateHandler.postDelayed(this, 100);

            if(!isCountdownOngoing) {
                statusUpdateElement.setText(countdownPlayer.getTimedownDisplay());
                deriveButtonState();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_play);
        Intent i = getIntent();
        String playFilePath = i.getStringExtra(HomeChooserActivity
                .HOME_PLAY_FILEPATH);
        String playFileDisplay = Utils.extractFilename(playFilePath.split("/"));
        setNowPlayingText(playFileDisplay);

        countTime = i.getIntExtra(HomeChooserActivity
                .HOME_COUNTDOWN_SECONDS, 4);
        statusUpdateElement = (TextView) findViewById(R.id.statusScreen);
        intervalMillis = 1000L;
        countdownPlayer = new CountdownPlayer(this, playFilePath);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        SharedPreferences sp = getSharedPreferences(getString(R.string
                .shared_preferences_key), Context.MODE_PRIVATE);
        appKVStore = new KVStore(sp);
        recentFiles = new LRUPriorityQueue(sp, 4, getString(R.string
                .kv_recent_files));

        playBtn = (ImageButton) findViewById(R.id.playBtn);
        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        stopBtn = (ImageButton) findViewById(R.id.stopBtn);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        forwardBtn = (ImageButton) findViewById(R.id.forwardBtn);

        Typeface led16Seg = FontCache.get("fonts/led16sgmnt-Regular.ttf", this);
        statusUpdateElement.setTypeface(led16Seg);

        setupStartState();
    }

    private void setupStartState(){
        disableAllButtons();
        setupSeekbar();
        beginCountdown();
    }

    // FIXME find a more idiomatic way of writing this.
    private void disableAllButtons(){
        playBtn.setEnabled(false);
        pauseBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        backBtn.setEnabled(false);
        forwardBtn.setEnabled(false);
    }

    private void deriveButtonState(){
        MediaPlayer mp = countdownPlayer.getMediaPlayer();
        playBtn.setEnabled(!mp.isPlaying());
        pauseBtn.setEnabled(mp.isPlaying());
        stopBtn.setEnabled(mp.isPlaying());
        backBtn.setEnabled(mp.isPlaying());
        forwardBtn.setEnabled(mp.isPlaying());
    }

    public void pressPlay(View v){
        countdownPlayer.play();
    }

    public void pressPause(View v){
        countdownPlayer.togglePause();
    }

    public void pressStop(View v){
        countdownPlayer.toggleStop();
    }

    private void setNowPlayingText(String playFileDisplay) {
        TextView nowPlaying = (TextView) findViewById(R.id.nowPlaying);
        String playText = getApplicationContext().getResources().getString(R
                .string.jamming_msg);
        nowPlaying.setText(playText + " " + playFileDisplay);
    }

    private void setupSeekbar(){
        MediaPlayer mp = countdownPlayer.getMediaPlayer();
        seekBar.setMax(mp.getDuration());
        seekBar.setProgress(mp.getCurrentPosition());
        seekUpdateHandler.postDelayed(uiUpdateRunner, 100);
    }

    private void beginCountdown() {
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,
                100);
        isCountdownOngoing = true;
        displayCount = countTime;
        Runnable r = new Runnable() {
            public void run() {
                while (displayCount >= 0) {
                    Log.i("counting", "counting " + displayCount);
                    runOnUiThread(new Runnable() {
                                      public void run() {
                                          statusUpdateElement.setText
                                                  (Integer.toString(displayCount));
                                          tg.startTone(ToneGenerator
                                                  .TONE_PROP_BEEP, 1000);
                                          displayCount--;
                                      }
                                  }
                    );
                    try {
                        Thread.sleep(intervalMillis);
                    } catch (InterruptedException ie) {
                        Log.e("beginCountdown", "InterruptedException " +
                                "occurred", ie);
                    }
                }
                Log.i("beginCountdown", "playing...");
                isCountdownOngoing = false;
                countdownPlayer.play();
            }
        };
        new Thread(r).start();
    }

    public void chooseJamSong(View v){
        String lastDirectory = appKVStore.get(getString(R.string
                .kv_last_directory), "/");
        Utils.createMaterialFilePicker(this, lastDirectory).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionsRequest.FILE_READ &&
                resultCode == RESULT_OK) {
            countdownPlayer.toggleStop();
            String filepath = data.getStringExtra(FilePickerActivity
                    .RESULT_FILE_PATH);
            String[] filepathComponents = filepath.split("/");
            String filename = Utils.extractFilename(filepathComponents);
            String lastDirectory = Utils.extractFilepath(filepathComponents);

            try{
                countdownPlayer.reset(filepath);
                appKVStore.set(getString(R.string.kv_last_directory), lastDirectory);
                setNowPlayingText(filename);

                recentFiles.enqueue(filepath);
                setupStartState();
            } catch(IOException ioe){
                Context c = getApplicationContext();
                String msg = c.getResources().getString
                        (R.string.file_not_found);
                Toast.makeText(c, msg, Toast.LENGTH_SHORT);
            }
        }
    }
}
