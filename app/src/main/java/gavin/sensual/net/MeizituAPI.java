package gavin.sensual.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 妹子图 Api
 *
 * @author gavin.xiong 2016/12/9
 */
public interface MeizituAPI {

    @Headers("Cache-Control: max-stale=1800")
    @GET("index.html")
    Observable<ResponseBody> getHome();

    @Headers("Cache-Control: max-stale=1800")
    @GET("a/{type}_{offset}.html")
    Observable<ResponseBody> get(@Path("type") String type, @Path("offset") int offset);
}
