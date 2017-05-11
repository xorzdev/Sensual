package gavin.sensual.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 知乎看图 Api
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ZhihuPicAPI {

    @Headers("authorization: oauth c3cef7c66a1843f8b3a9e6a1e3160e20")
    @GET("{question}/answers")
    Observable<ResponseBody> getAnswer(@Path("question") long question, @Query("include") String include, @Query("limit") int limit, @Query("offset") int offset);
}
