package gavin.sensual.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gavin.sensual.net.GankAPI;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * GankAPIModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class GankAPIModule {

    private static final String BASE_URL = "http://gank.io/api/";

    /**
     * 创建一个ClientAPI的实现类单例对象
     *
     * @param client           OkHttpClient
     * @param converterFactory Converter.Factory
     * @return ClientAPI
     */
    @Singleton
    @Provides
    public GankAPI provideGankApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(GankAPI.class);
    }

}
