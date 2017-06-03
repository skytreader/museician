package net.skytreader.museician;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CountdownPlayActivity extends AppCompatActivity {

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

        int countdownSeconds = i.getIntExtra(HomeChooserActivity
                .HOME_COUNTDOWN_SECONDS, 4);
        TextView statusScreen = (TextView) findViewById(R.id.statusScreen);
        countdown(statusScreen, countdownSeconds, 1);
    }

    private void countdown(TextView countDisplay, int countTime, int
            intervalSec){
        long intervalMillis = intervalSec * 1000;
        try {
            while (countTime > 0) {
                countDisplay.setText(Integer.toString(countTime));
                countTime--;
                Thread.sleep(intervalMillis);
            }
        } catch(InterruptedException ie){
            Log.e("countdown", "InterruptedException occurred", ie);
        }

    }
}
