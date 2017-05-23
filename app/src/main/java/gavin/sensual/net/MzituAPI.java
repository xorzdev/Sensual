package gavin.sensual.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * 妹子图 Api
 *
 * @author gavin.xiong 2016/12/9
 */
public interface MzituAPI {

    @Headers("Cache-Control: max-stale=1800")
    @GET("{type}/{offset1}/{offset2}/")
    Observable<ResponseBody> getPic(@Path("type") String type, @Path("offset1") String offset1, @Path("offset2") String offset2);
}
