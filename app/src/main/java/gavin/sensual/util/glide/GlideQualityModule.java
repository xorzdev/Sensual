package gavin.sensual.util.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * 自定义Glide模块 - 图片质量
 * -
 * - 需在 Manifest.xml 中配置
 * -    <meta-data
 * -        android:name="xxx.GlideQualityModule"
 * -        android:value="GlideModule" />
 * - 只能同时存在一个 Module 若要与 OkHttpGlideModule 等同时使用需合并)
 *
 * @author gavin.xiong 2016/9/14
 */
public class GlideQualityModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    /**
     * 使用 okhttp 下载图片
     * {@link com.bumptech.glide.integration.okhttp3.OkHttpGlideModule}
     */
    @Override
    public void registerComponents(Context context, Glide glide) {
        // 使用 okhttp 下载图片
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
