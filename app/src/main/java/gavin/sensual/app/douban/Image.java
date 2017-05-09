package gavin.sensual.app.douban;

import java.io.Serializable;

public class Image implements Serializable {

    private String id;
    private String time;
    private String url;

    private int width;
    private int height;

    public Image(String url) {
        this.url = url;
    }

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
        return "Image{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
