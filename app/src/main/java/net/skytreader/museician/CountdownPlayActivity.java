package net.skytreader.museician;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class CountdownPlayActivity extends AppCompatActivity {

    private CountdownPlayer countdownPlayer;

    private TextView statusUpdateElement;
    private SeekBar seekBar;
    private int countTime;
    private long intervalMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_play);
        Intent i = getIntent();
        String playFilePath = i.getStringExtra(HomeChooserActivity
                .HOME_PLAY_FILEPATH);
        String playFileDisplay = Utils.extractFilename(playFilePath.split("/"));
        TextView nowPlaying = (TextView) findViewById(R.id.nowPlaying);
        String playText = getApplicationContext().getResources().getString(R
                .string.jamming_msg);
        nowPlaying.setText(playText + " " + playFileDisplay);

        countTime = i.getIntExtra(HomeChooserActivity
                .HOME_COUNTDOWN_SECONDS, 4);
        statusUpdateElement = (TextView) findViewById(R.id.statusScreen);
        intervalMillis = 1000L;
        countdownPlayer = new CountdownPlayer(this, playFilePath);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        beginCountdown();
    }

    private void beginCountdown() {
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,
                100);
        Runnable r = new Runnable() {
            public void run() {
                while (countTime >= 0) {
                    runOnUiThread(new Runnable() {
                                      public void run() {
                                          statusUpdateElement.setText
                                                  (Integer.toString(countTime));
                                          tg.startTone(ToneGenerator
                                                  .TONE_PROP_BEEP, 1000);
                                          countTime--;
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
}
