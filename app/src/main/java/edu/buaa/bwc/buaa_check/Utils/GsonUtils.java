package edu.buaa.bwc.buaa_check.Utils;


import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by airhome on 2017/1/9.
 */

public class GsonUtils {
    private static Gson gson = new Gson();

    public static <T> List<T> jsonToList(String jsonStr, Class<T[]> clazz) {
        return Arrays.asList(gson.fromJson(jsonStr, clazz));
    }
}
