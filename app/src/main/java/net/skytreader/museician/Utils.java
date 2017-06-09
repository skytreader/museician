package net.skytreader.museician;

import android.app.Activity;

import com.nbsp.materialfilepicker.MaterialFilePicker;

/**
 * These are very project-specific utilities. Will mostly contain refactored
 * code.
 *
 * Created by chad on 6/4/17.
 */

public class Utils {

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
                .withHiddenFiles(false).withFilterDirectories(true).withPath
                        (lastDirectory);
    }

    public static String extractFilename(String[] filepathComponents) {
        return filepathComponents[filepathComponents.length - 1];
    }

}
