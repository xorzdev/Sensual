package gavin.sensual.app.capture.maijiaxiu;

import java.io.Serializable;
import java.util.List;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/10
 */
public class Maijiaxiu implements Serializable {

    private List<Model> list;

    public List<Model> getList() {
        return list;
    }

    public static class Model implements Serializable {

        private long id;
        private String src;
        private String title;
        private int star;

        public long getId() {
            return id;
        }

        public String getSrc() {
            return src;
        }

        public String getTitle() {
            return title;
        }
    }
}
