package edu.buaa.bwc.buaa_check.Utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by airhome on 2017/1/13.
 */

public class FileUtils {
    private static final String BASE_URL = "/BUAA_CHECK";
    private static final String IMAGE_URL = "/image";

    public static File getImageFile() {
        File dir = new File(Environment.getExternalStorageDirectory() + BASE_URL + IMAGE_URL);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return new File(dir, "JPEG_" + timeStamp + ".jpg");
    }

}
