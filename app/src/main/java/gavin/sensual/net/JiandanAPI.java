package gavin.sensual.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * 煎蛋 Api
 *
 * @author gavin.xiong 2016/12/9
 */
public interface JiandanAPI {

    // "ooxx/page-75#comments"
    @Headers("Cache-Control: max-stale=1800")
    @GET("ooxx/{offset}")
    Observable<ResponseBody> get(@Path("offset") String page);
}
