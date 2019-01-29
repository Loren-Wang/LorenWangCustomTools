package android.lorenwang.tools.image;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by LorenWang on 2018/8/2 0002.
 * 创建时间：2018/8/2 0002 上午 11:01
 * 创建人：王亮（Loren wang）
 * 功能作用：简单位图lrucache
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class SimpleBitmapLruCacheUtils{
    private final String TAG = getClass().getName();
    private LruCache<String,Bitmap> bitmapLruCache;
    private static SimpleBitmapLruCacheUtils simpleBitmapLruCacheUtils;
    public static SimpleBitmapLruCacheUtils getInstance(){
        if(simpleBitmapLruCacheUtils == null){
            simpleBitmapLruCacheUtils = new SimpleBitmapLruCacheUtils();
        }
        return simpleBitmapLruCacheUtils;
    }
    private SimpleBitmapLruCacheUtils() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        int cacheSize = maxMemory / 4;

        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return bitmapLruCache.get(key);
    }
}
