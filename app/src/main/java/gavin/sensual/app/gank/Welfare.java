package gavin.sensual.app.gank;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 这里是萌萌哒注释君
 * <p>
 * "_id": "590bce25421aa90c7d49ad3c",
 * "createdAt": "2017-05-05T08:58:13.502Z",
 * "desc": "5-5",
 * "publishedAt": "2017-05-05T11:56:35.629Z",
 * "source": "chrome",
 * "type": "\u798f\u5229",
 * "url": "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg",
 * "used": true,
 * "who": "daimajia"
 *
 * @author gavin.xiong 2017/5/6
 */
public class Welfare implements Serializable {

    @SerializedName("_id")
    private String id;
    @SerializedName("publishedAt")
    private String time;
    @SerializedName("url")
    private String url;

    private int width;
    private int height;

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Welfare{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
