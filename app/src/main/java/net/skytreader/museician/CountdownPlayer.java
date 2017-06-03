package net.skytreader.museician;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chad on 6/4/17.
 */

public class CountdownPlayer extends AsyncTask<Void, Void, Void> {

    private TextView statusUpdateElement;
    private Activity activity;
    private int countTime;
    private long intervalMillis;

    public CountdownPlayer(Activity a, TextView sue, int countTime, long
            intervalMillis){
        activity = a;
        statusUpdateElement = sue;
        this.countTime = countTime;
        this.intervalMillis = intervalMillis;
    }

    @Override
    protected Void doInBackground(Void... params){
        try {
            while (countTime >= 0) {
                statusUpdateElement.setText(Integer.toString(countTime));
                countTime--;
                Thread.sleep(intervalMillis);
            }
        } catch(InterruptedException ie){
            Log.e("countdown", "InterruptedException occurred", ie);
        }
        return null;
    }
}
