package gavin.sensual.util.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    public OKHttpCacheInterceptor() {
        this.cacheControl = new CacheControl.Builder()
                .maxAge(60, TimeUnit.SECONDS) // 这个是控制缓存的最大生命时间
                .maxStale(86400, TimeUnit.SECONDS) // 这个是控制缓存的过时时间
                .build();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkAvailable(App.getApplication())) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
//                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
//        Response response = chain.proceed(request);
//        if (isNetworkAvailable(App.getApplication())) {
//            int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
//            return response.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + maxAge)
//                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                    .build();
//        } else {
//            int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//            return response.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                    .removeHeader("Pragma")
//                    .build();
//        }
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
