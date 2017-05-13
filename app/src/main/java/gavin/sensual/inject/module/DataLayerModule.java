package gavin.sensual.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gavin.sensual.service.DailyManager;
import gavin.sensual.service.DoubanManager;
import gavin.sensual.service.GankManager;
import gavin.sensual.service.MeiziManager;
import gavin.sensual.service.ZhihuPicManager;
import gavin.sensual.service.base.DataLayer;

/**
 * DataLayerModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class DataLayerModule {

    @Singleton
    @Provides
    public DailyManager provideDailyManager() {
        return new DailyManager();
    }

    @Singleton
    @Provides
    public GankManager provideGankManager() {
        return new GankManager();
    }

    @Singleton
    @Provides
    public DoubanManager provideDoubanManager() {
        return new DoubanManager();
    }

    @Singleton
    @Provides
    public ZhihuPicManager provideZhihuPicAManager() {
        return new ZhihuPicManager();
    }

    @Singleton
    @Provides
    public MeiziManager provideMeiziManager() {
        return new MeiziManager();
    }

    @Singleton
    @Provides
    public DataLayer provideDataLayer(DailyManager dailyManager, GankManager gankManager, DoubanManager doubanManager, ZhihuPicManager zhihuPicManager, MeiziManager meiziManager) {
        return new DataLayer(dailyManager, gankManager, doubanManager, zhihuPicManager, meiziManager);
    }
}
