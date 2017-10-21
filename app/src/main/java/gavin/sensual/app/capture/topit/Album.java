package gavin.sensual.app.capture.topit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Album
 *
 * @author gavin.xiong 2017/10/13
 */
public class Album implements Serializable {

    private Info info;
    private List<Item> item;

    public Info getInfo() {
        return info;
    }

    public List<Item> getItem() {
        return item;
    }

    public static class Info implements Serializable {

        private int num;

        public int getNum() {
            return num;
        }

    }

    public static class Item implements Serializable {

        private long id;

        private Icon icon;

        public long getId() {
            return id;
        }

        public Icon getIcon() {
            return icon;
        }

    }

    public static class Icon implements Serializable {

        @SerializedName(value = "url_l", alternate = "url")
        private String url;
        @SerializedName(value = "l_width", alternate = "width")
        private int width;
        @SerializedName(value = "l_height", alternate = "height")
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

    }
}
