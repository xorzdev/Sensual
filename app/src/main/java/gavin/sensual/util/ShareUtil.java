package gavin.sensual.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import gavin.sensual.base.App;
import gavin.sensual.base.CacheHelper;

/**
 * 分享助手
 *
 * @author gavin.xiong 2017/3/21
 */
public class ShareUtil {

//    /**
//     * 通过imageView已有的图片分享Image
//     */
//    public static void shareImage(Context context, ImageView imageView) {
//        try {
//            GlideBitmapDrawable bd = (GlideBitmapDrawable) imageView.getDrawable();
//            String path = CacheHelper.getCacheDir(context) + "temp_share.jpg";
//            compressImage(bd.getBitmap(), path);
//
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            Uri uri = Uri.fromFile(new File(path));
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//            intent.setType("image/*");
//            context.startActivity(Intent.createChooser(intent, "分享"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 通过imageView已有的图片分享Image
     */
    public static void shareImage(Fragment fragment, String url) throws Exception {
        String path = CacheHelper.getCacheDir(App.getApplication()) + File.separator +"share.jpg";
        compressImage(ImageLoader.getBitmap(fragment, url), path);
        tryShare(fragment, path);
    }

    private static void tryShare(Fragment fragment, String path) throws Exception {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(new File(path));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        fragment.startActivity(Intent.createChooser(intent, "分享"));
    }

    /**
     * 存储大小的缩放
     */
    private static void compressImage(Bitmap image, String path) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 85;
            while ((baos.toByteArray().length / 1024 > 200) && (options > 1)) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
                baos.reset(); // 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            }
            baos.writeTo(fileOutputStream);
            // 用完了记得回收
//            image.recycle();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                fileOutputStream.close();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
