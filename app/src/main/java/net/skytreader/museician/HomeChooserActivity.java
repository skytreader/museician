package net.skytreader.museician;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nbsp.materialfilepicker.MaterialFilePicker;

public class HomeChooserActivity extends AppCompatActivity {

    public static final String HOME_COUNTDOWN_SECONDS = "net.skytreader.museician.HomeChooserActivity.HOME_COUNTDOWN_SECONDS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chooser);
        EditText countdownSecondsET = (EditText) findViewById(R.id.countdownSeconds);
        countdownSecondsET.setText("4");
    }

    public void startJamming(View view){
        Intent intent = new Intent(this, CountdownPlayActivity.class);
        EditText countdownSecondsET = (EditText) findViewById(R.id.countdownSeconds);
        int countdownSeconds = Integer.parseInt(countdownSecondsET.getText().toString());
        intent.putExtra(HomeChooserActivity.HOME_COUNTDOWN_SECONDS, countdownSeconds);
        startActivity(intent);
    }

    public void chooseJamSong(View view){
        new MaterialFilePicker().withActivity(this).withRequestCode(1).withHiddenFiles(false).start();
    }
}
