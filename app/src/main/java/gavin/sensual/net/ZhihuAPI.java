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
public interface ZhihuAPI {

    @Headers({
//            "authorization: oauth c3cef7c66a1843f8b3a9e6a1e3160e20",
            "authorization: Bearer Mi4wQUFCQUJfc3ZBQUFBWU1EQTZSaVFDaGNBQUFCaEFsVk5jLU04V1FDMkZkSVJjenJGQlNxN0tvSTdRWXZJQmZMU0t3|1495604553|0c6ead0d7cb6ca026f92665ab2e0976d831c9906",
            "Cache-Control: max-stale=3600"
    })
    @GET("api/v4/questions/{id}/answers")
    Observable<ResponseBody> getAnswer(@Path("id") long id, @Query("include") String include, @Query("limit") int limit, @Query("offset") int offset);

    @Headers("Cache-Control: max-stale=3600")
    @GET("collection/{id}")
    Observable<ResponseBody> getCollection(@Path("id") long id, @Query("page") int offset);
}
