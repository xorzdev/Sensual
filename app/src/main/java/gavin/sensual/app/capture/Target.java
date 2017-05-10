package gavin.sensual.app.capture;

import java.io.Serializable;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/10
 */
public class Target implements Serializable {

    private String title;
    private String url;
    private String image;

    public Target(String title, String url, String image) {
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }
}
