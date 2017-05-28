package net.skytreader.museician;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;

public class HomeChooserActivity extends AppCompatActivity {

    public static final String HOME_COUNTDOWN_SECONDS = "net.skytreader" +
            ".museician.HomeChooserActivity.HOME_COUNTDOWN_SECONDS";
    public static final int PERMISSIONS_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chooser);
        EditText countdownSecondsET = (EditText) findViewById(R.id
                .countdownSeconds);
        countdownSecondsET.setText("4");
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest
                        .permission.READ_EXTERNAL_STORAGE},
                        HomeChooserActivity.PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        switch (requestCode) {
            case HomeChooserActivity.PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Context c = getApplicationContext();
                    Toast t = Toast.makeText(c, "Please grant file access to " +
                            "Museician. Functionality might be limited " +
                            "otherwise.", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
    }

    public void startJamming(View view) {
        Intent intent = new Intent(this, CountdownPlayActivity.class);
        EditText countdownSecondsET = (EditText) findViewById(R.id
                .countdownSeconds);
        int countdownSeconds = Integer.parseInt(countdownSecondsET.getText()
                .toString());
        intent.putExtra(HomeChooserActivity.HOME_COUNTDOWN_SECONDS,
                countdownSeconds);
        startActivity(intent);
    }

    public void chooseJamSong(View view) {
        new MaterialFilePicker().withActivity(this).withRequestCode(1)
                .withHiddenFiles(false).withFilterDirectories(true).start();
    }
}
