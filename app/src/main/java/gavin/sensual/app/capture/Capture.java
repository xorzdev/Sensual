package gavin.sensual.app.capture;

import java.io.Serializable;

/**
 * 发现
 *
 * @author gavin.xiong 2017/5/24
 */
public class Capture implements Serializable {

    private long id;
    private String title;
    private String image;

    public Capture(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
