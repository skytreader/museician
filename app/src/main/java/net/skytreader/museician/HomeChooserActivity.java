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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

public class HomeChooserActivity extends AppCompatActivity {

    public static final String HOME_COUNTDOWN_SECONDS = "HOME_COUNTDOWN_SECONDS";
    public static final String HOME_PLAY_FILEPATH = "HOME_PLAY_FILEPATH";
    public static final int FILE_READ_REQUEST_CODE = 0;

    private String playFilePath;

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
                        HomeChooserActivity.FILE_READ_REQUEST_CODE);
            }
        }
    }

    private String extractFilename(String filepath) {
        String[] parse = filepath.split("/");
        return parse[parse.length - 1];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HomeChooserActivity.FILE_READ_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            Button startJamming = (Button) findViewById(R.id.startJamming);
            String filepath = data.getStringExtra(FilePickerActivity
                    .RESULT_FILE_PATH);
            String filename = extractFilename(filepath);
            playFilePath = filename;
            String newHint = getApplicationContext().getResources().getString
                    (R.string.start_jam_cta);
            startJamming.setHint(newHint + " " + filename);
            startJamming.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        switch (requestCode) {
            case HomeChooserActivity.FILE_READ_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Context c = getApplicationContext();
                    String filePermissionsMsg = c.getResources().getString(R
                            .string.file_permission_msg);
                    Toast t = Toast.makeText(c, filePermissionsMsg, Toast
                            .LENGTH_SHORT);
                    t.show();
                }
            }
        }
    }

    public void startJamming(View view) {
        playFilePath = playFilePath.trim();
        if (playFilePath == null || playFilePath.length() <= 0) {
            throw new RuntimeException("startJamming called while " +
                    "playFilePath is not properly set.");
        }
        Intent intent = new Intent(this, CountdownPlayActivity.class);
        EditText countdownSecondsET = (EditText) findViewById(R.id
                .countdownSeconds);
        int countdownSeconds = Integer.parseInt(countdownSecondsET.getText()
                .toString());
        intent.putExtra(HomeChooserActivity.HOME_COUNTDOWN_SECONDS,
                countdownSeconds);
        intent.putExtra(HOME_PLAY_FILEPATH, playFilePath);
        startActivity(intent);
    }

    public void chooseJamSong(View view) {
        new MaterialFilePicker().withActivity(this).withRequestCode
                (HomeChooserActivity.FILE_READ_REQUEST_CODE)
                .withHiddenFiles(false).withFilterDirectories(true).start();
    }
}
