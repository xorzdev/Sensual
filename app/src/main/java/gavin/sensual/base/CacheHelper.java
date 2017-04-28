package gavin.sensual.base;

import android.content.Context;
import android.os.Environment;

/**
 * 缓存助手
 *
 * @author gavin.xiong 2017/4/28
 */
public class CacheHelper {

    /**
     * 获取缓存文件夹 ( sdCard
     * -    ? /storage/sdcard0/Android/data/com.example.dir/cache
     * -    : /data/data/com.example.dir/cache )
     *
     * @param context
     * @return
     */
    public static String getCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取缓存文件夹 ( sdCard
     * -    ? /storage/sdcard0/Android/data/com.example.dir/files
     * -    : /data/data/com.example.dir/files )
     *
     * @param context
     * @param type 传空在 files 根目录下， 传值在 files 下创建对应文件夹
     * @return
     */
    public static String getFilesDir(Context context, String type) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(type).getPath();
        }
        return cachePath;
    }
}
