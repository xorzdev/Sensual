package gavin.sensual.util.okhttp;

import java.io.IOException;

import gavin.sensual.util.L;
import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * OkHttp3 网络拦截器 - 图片路径修正 & 反反盗链
 *
 * @author gavin.xiong 2017/4/28
 */
public final class OKHttpRefererNetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String url = chain.request().url().toString();
        if (url.matches("http://i.meizitu.net[A-Za-z0-9/]+\\.(jpg|jpeg|gif|png)")) {
            // Mzitu
            return chain.proceed(chain.request()
                    .newBuilder()
                    .header("Referer", "http://m.mzitu.com/")
                    .build());
        } else if (url.matches("http://[A-Za-z0-9/]{2}\\.topitme.com[A-Za-z0-9/]+\\.(jpg|jpeg|gif|png)")) {
            L.e("匹配优美图");
            // topit.me
            return chain.proceed(chain.request()
                    .newBuilder()
                    .url(url.replaceFirst(".topitme.com", ".topitme.com/"))
                    .build());
        } else {
            return chain.proceed(chain.request());
        }
    }

}
