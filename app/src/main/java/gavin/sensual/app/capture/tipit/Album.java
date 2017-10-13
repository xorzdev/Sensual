package gavin.sensual.app.capture.tipit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Album
 *
 * @author gavin.xiong 2017/10/13
 */
public class Album implements Serializable {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public static class Item implements Serializable {

        private Icon icon;
        private String next;

        public Icon getIcon() {
            return icon;
        }

        public String getNext() {
            return next;
        }
    }


    public static class Icon implements Serializable {

        @SerializedName("url_l")
        private String url;
        @SerializedName("l_width")
        private int width;
        @SerializedName("l_height")
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
