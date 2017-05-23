package gavin.sensual.service.base;

import com.google.gson.Gson;

import javax.inject.Inject;

import gavin.sensual.inject.component.ApplicationComponent;
import gavin.sensual.net.ClientAPI;
import gavin.sensual.net.DoubanAPI;
import gavin.sensual.net.GankAPI;
import gavin.sensual.net.MeizituAPI;
import gavin.sensual.net.MzituAPI;
import gavin.sensual.net.ZhihuPicAPI;

/**
 * BaseManager
 *
 * @author gavin.xiong 2017/4/28
 */
public abstract class BaseManager {
    @Inject
    ClientAPI mApi;
    @Inject
    GankAPI mGankApi;
    @Inject
    DoubanAPI mDoubanApi;
    @Inject
    ZhihuPicAPI mZhihuPicApi;
    @Inject
    MzituAPI mMeiziAPI;
    @Inject
    MeizituAPI mMeizituAPI;
    @Inject
    Gson mGson;

    public BaseManager() {
        ApplicationComponent.Instance.get().inject(this);
    }

    public ClientAPI getApi() {
        return mApi;
    }

    public GankAPI getGankApi() {
        return mGankApi;
    }

    public DoubanAPI getDoubanApi() {
        return mDoubanApi;
    }

    public ZhihuPicAPI getZhihuPicApi() {
        return mZhihuPicApi;
    }

    public MzituAPI getMzituAPI() {
        return mMeiziAPI;
    }

    public MeizituAPI getMeizituAPI() {
        return mMeizituAPI;
    }

    public Gson getGson() {
        return mGson;
    }
}
