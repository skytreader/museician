package net.skytreader.museician;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.Arrays;

public class HomeChooserActivity extends AppCompatActivity {

    public static final String HOME_COUNTDOWN_SECONDS =
            "HOME_COUNTDOWN_SECONDS";
    public static final String HOME_PLAY_FILEPATH = "HOME_PLAY_FILEPATH";
    public static final int FILE_READ_REQUEST_CODE = 0;

    private final int RECENCY_LIMIT = 4;

    private String playFilePath;

    private String lastDirectory;
    private String[] mostRecentFiles;

    private SharedPreferences kvstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chooser);

        Context appContext = getApplicationContext();

        EditText countdownSecondsET = (EditText) findViewById(R.id
                .countdownSeconds);
        countdownSecondsET.setText(appContext.getResources()
                .getString(R.string.countdown_default));

        // Check (and get) the permission for reading external storage.
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

        // Check (and get) related data from SharedPreferences KV-Store.
        // NOTE: Android tutorial uses the Context from getActivity. I wonder
        // how this is different.
        kvstore = appContext.getSharedPreferences(getString
                (R.string.shared_preferences_key), Context.MODE_PRIVATE);
        lastDirectory = kvstore.getString(getString(R.string
                .kv_last_directory), null);
        mostRecentFiles = constructMostRecentFilenames(kvstore, RECENCY_LIMIT);
    }

    private String[] constructMostRecentFilenames(SharedPreferences kvstore,
                                                  int recencyCount) {
        String[] recentFilenames = new String[recencyCount];

        for (int i = 0; i < recencyCount; i++) {
            recentFilenames[i] = kvstore.getString(getString(R.string
                    .kv_recent_files) + Integer.toString(i + 1), null);
        }
        return recentFilenames;
    }

    private String extractFilename(String[] filepathComponents) {
        return filepathComponents[filepathComponents.length - 1];
    }

    private String extractFilepath(String[] filepathComponents) {
        return TextUtils.join("/",
                Arrays.copyOf(filepathComponents, filepathComponents.length -
                        1));
    }

    private void saveLastDirectory(String lastDirectory) {
        SharedPreferences.Editor kvEditor = kvstore.edit();
        kvEditor.putString(getString(R.string.kv_last_directory),
                lastDirectory);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HomeChooserActivity.FILE_READ_REQUEST_CODE &&
                resultCode == RESULT_OK) {
            Button startJamming = (Button) findViewById(R.id.startJamming);
            String filepath = data.getStringExtra(FilePickerActivity
                    .RESULT_FILE_PATH);
            String[] filepathComponents = filepath.split("/");
            String filename = extractFilename(filepathComponents);
            playFilePath = filepath;
            lastDirectory = extractFilepath(filepathComponents);

            // Set to
            saveLastDirectory(lastDirectory);

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
