package anup.project;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Caching {
    static HashMap<String,Bitmap> cacheMap=new HashMap<String,Bitmap>();

    public static void add(String url, Bitmap bitmap) {
        cacheMap.put(url,bitmap);
    }

    public static Bitmap get(String url) {
        return cacheMap.get(url);
    }
}
