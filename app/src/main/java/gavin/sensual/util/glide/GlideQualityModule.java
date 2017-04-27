package gavin.sensual.util.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * 自定义Glide模块 - 图片质量
 * -
 * - 需在 Manifest.xml 中配置
 * -    <meta-data
 * -        android:name="com.gavin.utils.glide.GlideQualityModule"
 * -        android:value="GlideModule" />
 * - 只能同时存在一个 Module 若要与 OkHttpGlideModule 等同时存在只需把各 Module 合并成)
 *
 * @author gavin.xiong 2016/9/14
 */
public class GlideQualityModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
