package gavin.sensual.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import gavin.sensual.base.App;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp3 缓存拦截器
 *
 * @author gavin.xiong 2017/4/28
 */
public class OKHttpCacheInterceptor implements Interceptor {

    private CacheControl cacheControl;

    public OKHttpCacheInterceptor(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkAvailable(App.getApplication())) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (isNetworkAvailable(App.getApplication())) {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", request.cacheControl().toString())
                    .build();
        } else {
//            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            int maxStale = 10; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }

    /**
     * 判断网络是否有效
     */
    private boolean isNetworkAvailable(Context context) {
        NetworkInfo netInfo = getNetworkInfo(context);
        return netInfo != null && netInfo.isAvailable();
    }

    /**
     * 获取当前网络信息
     */
    private NetworkInfo getNetworkInfo(Context context) {
        if (null != context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return mConnectivityManager.getActiveNetworkInfo();
        }
        return null;
    }
}
