package gavin.sensual.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gavin.sensual.net.DoubanAPI;
import gavin.sensual.net.GankAPI;
import gavin.sensual.net.JiandanAPI;
import gavin.sensual.net.MeizituAPI;
import gavin.sensual.net.MzituAPI;
import gavin.sensual.net.ZhihuAPI;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * ClientAPIModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class OthersAPIModule {

    /**
     * 干货集中营妹子
     */
    @Singleton
    @Provides
    public GankAPI provideGankApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(GankAPI.class);
    }

    /**
     * 豆瓣妹子
     */
    @Singleton
    @Provides
    public DoubanAPI provideDoubanApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.dbmeinv.com/dbgroup/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(DoubanAPI.class);
    }

    /**
     * 妹子图 - mzitu
     */
    @Singleton
    @Provides
    public MzituAPI provideMzituApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mzitu.com/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(MzituAPI.class);
    }

    /**
     * 妹子图 - meizitu
     */
    @Singleton
    @Provides
    public MeizituAPI provideMeizituApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.meizitu.com/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(MeizituAPI.class);
    }

    /**
     * 知乎看图
     */
    @Singleton
    @Provides
    public ZhihuAPI provideZhihuPicApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.zhihu.com/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ZhihuAPI.class);
    }

    /**
     * 煎蛋妹子
     */
    @Singleton
    @Provides
    public JiandanAPI provideJiandanApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jandan.net/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(JiandanAPI.class);
    }

}
