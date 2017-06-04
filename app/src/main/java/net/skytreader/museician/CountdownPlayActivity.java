package net.skytreader.museician;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CountdownPlayActivity extends AppCompatActivity {

    private CountdownPlayer countdownPlayer;

    private TextView statusUpdateElement;
    private int countTime;
    private long intervalMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_play);
        Intent i = getIntent();
        String playFilePath = i.getStringExtra(HomeChooserActivity
                .HOME_PLAY_FILEPATH);
        TextView nowPlaying = (TextView) findViewById(R.id.nowPlaying);
        String playText = getApplicationContext().getResources().getString(R
                .string.jamming_msg);
        nowPlaying.setText(playText + " " + playFilePath);

        countTime = i.getIntExtra(HomeChooserActivity
                .HOME_COUNTDOWN_SECONDS, 4);
        statusUpdateElement = (TextView) findViewById(R.id.statusScreen);
        intervalMillis = 1000L;
        beginCountdown();
    }

    private void beginCountdown() {
        Runnable r = new Runnable() {
            public void run() {
                while (countTime >= 0) {
                    runOnUiThread(new Runnable() {
                                      public void run() {
                                          statusUpdateElement.setText
                                                  (Integer.toString(countTime));
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
            }
        };
        new Thread(r).start();
    }
}
