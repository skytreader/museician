package net.skytreader.museician;

import android.app.Activity;

import com.nbsp.materialfilepicker.MaterialFilePicker;

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

}
