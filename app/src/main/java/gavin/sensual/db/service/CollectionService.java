package gavin.sensual.db.service;

import java.util.List;

import gavin.sensual.app.collection.Collection;
import gavin.sensual.db.dao.CollectionDao;
import gavin.sensual.util.L;

/**
 * 收藏 service
 *
 * @author gavin.xiong 2016/9/2
 */
public class CollectionService extends BaseService<Collection, Long> {

    public CollectionService(CollectionDao dao) {
        super(dao);
    }

    public void save(String image) {
        Collection t = new Collection();
        t.setImage(image);
        t.setTime(System.currentTimeMillis());
        save(t);
    }

    public void delete(String image) {
        queryBuilder().where(CollectionDao.Properties.Image.eq(image)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public boolean hasCollected(String image) {
        return queryBuilder().where(CollectionDao.Properties.Image.eq(image)).limit(1).buildCount().count() > 0;
    }

    public void toggle(String image) {
        if (hasCollected(image)) {
            delete(image);
        } else {
            save(image);
        }
    }

    public List<Collection> queryDesc(int offset) {
        L.e(offset * 10);
        return queryBuilder().orderDesc(CollectionDao.Properties.Id).limit(10).offset(offset * 10).list();
    }
}
