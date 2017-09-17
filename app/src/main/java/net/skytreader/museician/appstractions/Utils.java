package net.skytreader.museician.appstractions;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.nbsp.materialfilepicker.MaterialFilePicker;

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

    public static Map<String, String> getMp3Metadata(String filepath){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(filepath);
        Map<String, String> metadata = new HashMap<>();
        // TODO Find a way to type/enumerate the keys of this Map.
        metadata.put("filename", Utils.extractFilename(filepath.split("/")));
        // FIXME Handle blank metadata items.
        metadata.put("titleArtist", metadataRetriever.extractMetadata
                (MediaMetadataRetriever.METADATA_KEY_TITLE) + " - " +
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

        return metadata;
    }
}
