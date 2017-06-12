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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.Arrays;

public class HomeChooserActivity extends AppCompatActivity {

    public static final String HOME_COUNTDOWN_SECONDS =
            "HOME_COUNTDOWN_SECONDS";
    public static final String HOME_PLAY_FILEPATH = "HOME_PLAY_FILEPATH";

    private final int RECENCY_LIMIT = 4;

    private String playFilePath;

    private String lastDirectory;
    private String[] mostRecentFiles;

    private SharedPreferences _kvstore;
    private KVStore kvstore;
    private LRUPriorityQueue recentFiles;

    class RecentFilesItemClickListener implements AdapterView
            .OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int
                position, long id) {
            // TODO Check Android docs for ArrayAdapter and AdapterView for
            // better handling of this (i.e., don't require outer class stuff).
            String chosenFile = HomeChooserActivity.this
                    .mostRecentFiles[position];
            HomeChooserActivity.this.refreshJamButtonHint(Utils
                    .extractFilename(chosenFile.split("/")));
            HomeChooserActivity.this.playFilePath = chosenFile;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chooser);

        Context appContext = getApplicationContext();

        EditText countdownSecondsET = (EditText) findViewById(R.id
                .countdownSeconds);
        countdownSecondsET.setText(appContext.getResources()
                .getString(R.string.countdown_default));

        checkAndAskReadingPermission();

        // Check (and get) related data from SharedPreferences KV-Store.
        // NOTE: Android tutorial uses the Context from getActivity. I wonder
        // how this is different.
        _kvstore = appContext.getSharedPreferences(getString
                (R.string.shared_preferences_key), Context.MODE_PRIVATE);
        recentFiles = new LRUPriorityQueue(_kvstore, RECENCY_LIMIT, getString(R.string
                .kv_recent_files));
        kvstore = new KVStore(_kvstore);
        // TODO: Maybe we can just getString whenever we need lastDirectory?
        // TODO do away with the field variable.
        lastDirectory = kvstore.get(getString(R.string.kv_last_directory), "/");
        mostRecentFiles = recentFiles.getContents();
        Log.i("recentFiles", Arrays.toString(mostRecentFiles));
        constructRecentFilesListView(appContext, mostRecentFiles);
        ListView recentFiles = (ListView) findViewById(R.id.recentFilesList);
        recentFiles.setOnItemClickListener(new RecentFilesItemClickListener());
    }

    private void constructRecentFilesListView(Context appContext, String[]
            recentFilepaths){
        String[] recentFilenames = new String[recentFilepaths.length];
        for(int i = 0; i < recentFilepaths.length; i++){
            recentFilenames[i] = Utils.extractFilename(recentFilepaths[i]
                    .split("/"));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(appContext, android.R
                .layout.simple_list_item_1, recentFilenames);
        ListView recentFiles = (ListView) findViewById(R.id.recentFilesList);
        recentFiles.setAdapter(adapter);
    }

    private void checkAndAskReadingPermission(){
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest
                                .permission.READ_EXTERNAL_STORAGE},
                        PermissionsRequest.FILE_READ);
            }
        }
    }

    private String extractFilepath(String[] filepathComponents) {
        return TextUtils.join("/",
                Arrays.copyOf(filepathComponents, filepathComponents.length -
                        1));
    }

    private void saveLastDirectory(String lastDirectory) {
        this.lastDirectory = lastDirectory;
        kvstore.set(getString(R.string.kv_last_directory), lastDirectory);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionsRequest.FILE_READ &&
                resultCode == RESULT_OK) {
            String filepath = data.getStringExtra(FilePickerActivity
                    .RESULT_FILE_PATH);
            String[] filepathComponents = filepath.split("/");
            String filename = Utils.extractFilename(filepathComponents);
            lastDirectory = extractFilepath(filepathComponents);
            playFilePath = filepath;

            saveLastDirectory(lastDirectory);
            recentFiles.enqueue(filepath);
            refreshJamButtonHint(filename);
        }
    }

    private void refreshJamButtonHint(String filename){
        Button startJamming = (Button) findViewById(R.id.startJamming);
        String newHint = getApplicationContext().getResources().getString
                (R.string.start_jam_cta);
        startJamming.setHint(newHint + " " + filename);
        startJamming.setEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsRequest.FILE_READ: {
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
        Utils.createMaterialFilePicker(this, lastDirectory).start();
    }
}
