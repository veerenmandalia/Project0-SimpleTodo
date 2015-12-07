package com.codepath.simpletodo.utility;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileUtility {
    private static final String TAG = FileUtility.class.getSimpleName();

    private FileUtility() {
    }

    public static ArrayList<String> readItems(Context context, String filename) {
        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, filename);
        try {
            return new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            Log.e(TAG, "Error reading file, creating new list", e);
            return new ArrayList<>();
        }
    }

    public static void writeItems(Context context, List<String> items, String filename) {
        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, filename);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }
}
