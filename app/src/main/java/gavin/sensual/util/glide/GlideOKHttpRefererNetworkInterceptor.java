package gavin.sensual.util.glide;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * Glide & OkHttp3 网络拦截器 - 反反盗链
 *
 * @author gavin.xiong 2017/4/28
 */
class GlideOKHttpRefererNetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain.request().url().toString().startsWith("http://i.meizitu.net")) {
            return chain.proceed(chain.request()
                    .newBuilder()
                    .header("Referer", "http://m.mzitu.com/")
                    .build());
        } else {
            return chain.proceed(chain.request());
        }
    }

}
