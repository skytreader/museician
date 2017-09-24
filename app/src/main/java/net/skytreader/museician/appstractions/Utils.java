package net.skytreader.museician.appstractions;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.nbsp.materialfilepicker.MaterialFilePicker;

import net.skytreader.museician.R;
import net.skytreader.museician.appstractions.PermissionsRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * These are very project-specific utilities. Will mostly contain refactored
 * code.
 * <p>
 * Created by chad on 6/4/17.
 */

public class Utils {

    /**
     * Enumerates the keys the fields that will be retrieved from MP3 files.
     */
    public enum RetrievedMetadata {
        FILENAME("filename"), TITLE_ARTIST("titleArtist");

        private final String key;

        RetrievedMetadata(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static Pattern MP3_FILES = Pattern.compile(".*\\.mp3");

    /**
     * Creates an instance of MaterialFilePicker with sensible defaults.
     *
     * @param a
     * @param lastDirectory
     * @return MaterialFilePicker you can just call start() on.
     */
    public static MaterialFilePicker createMaterialFilePicker(Activity a,
                                                              String lastDirectory) {
        return new MaterialFilePicker().withActivity(a).withRequestCode
                (PermissionsRequest.FILE_READ)
                .withFilter(MP3_FILES).withHiddenFiles(false).withPath
                        (lastDirectory);
    }

    public static String extractFilename(String[] filepathComponents) {
        return filepathComponents[filepathComponents.length - 1];
    }

    public static String extractFilepath(String[] filepathComponents) {
        return TextUtils.join("/",
                Arrays.copyOf(filepathComponents, filepathComponents.length -
                        1));
    }

    private static String makeDefaultString(String original, String
            defaultReplacement) {
        return original != null ? original : defaultReplacement;
    }

    /**
     * Get metadata details from the MP3 file described in the given filepath.
     *
     * @param filepath
     * @return A Map whose keys are as enumerated in the Utils.RetrievedMetadata
     * enum.
     */
    public static Map<String, String> getMp3Metadata(String filepath, Context
            c) {
        String defaultNoArtist = c.getResources().getString(R.string
                .default_noartist);
        String defaultNoTitle = c.getResources().getString(R.string
                .default_notitle);
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(filepath);
        Map<String, String> metadata = new HashMap<>();
        metadata.put(RetrievedMetadata.FILENAME.getKey(), Utils.extractFilename
                (filepath.split("/")));
        metadata.put(RetrievedMetadata.TITLE_ARTIST.getKey(),
                makeDefaultString(metadataRetriever.extractMetadata
                        (MediaMetadataRetriever.METADATA_KEY_TITLE),
                        defaultNoTitle) +
                        " - " +
                        makeDefaultString(metadataRetriever.extractMetadata
                                (MediaMetadataRetriever
                                .METADATA_KEY_ARTIST), defaultNoArtist));

        return metadata;
    }
}
