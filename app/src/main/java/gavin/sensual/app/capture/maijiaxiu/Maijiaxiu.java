package gavin.sensual.app.capture.maijiaxiu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/8/10
 */
public class Maijiaxiu {

    @SerializedName("list")
    private List<Model> list;

    public List<Model> getList() {
        return list;
    }

    public static class Model {

        @SerializedName("id")
        private long id;
        @SerializedName("src")
        private String src;
        @SerializedName("title")
        private String title;

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
