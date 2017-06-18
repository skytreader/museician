package net.skytreader.museician;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

public class CountdownPlayActivity extends AppCompatActivity {

    private CountdownPlayer countdownPlayer;

    private TextView statusUpdateElement;
    private SeekBar seekBar;
    private int countTime;
    private int displayCount;
    private long intervalMillis;
    private KVStore appKVStore;
    private LRUPriorityQueue recentFiles;

    private Handler seekUpdateHandler = new Handler();
    private Runnable seekUpdateRunner = new Runnable(){
        @Override
        public void run() {
            seekBar.setProgress(countdownPlayer.getMediaPlayer().getCurrentPosition());
            seekUpdateHandler.postDelayed(this, 100);
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
        setupSeekbar();
        beginCountdown();
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
        seekUpdateHandler.postDelayed(seekUpdateRunner, 100);
    }

    private void beginCountdown() {
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,
                100);
        displayCount = countTime;
        Runnable r = new Runnable() {
            public void run() {
                while (displayCount >= 0) {
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
                countdownPlayer.execute(null, null);
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

            appKVStore.set(getString(R.string.kv_last_directory), lastDirectory);
            setNowPlayingText(filename);
            countdownPlayer = new CountdownPlayer(this, filepath);
            recentFiles.enqueue(filepath);
            beginCountdown();
        }
    }
}
