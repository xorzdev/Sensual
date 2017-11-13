package gavin.sensual.app.capture;

import com.google.gson.annotations.SerializedName;

/**
 * 发现
 *
 * @author gavin.xiong 2017/5/24
 */
public class Capture {

    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
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
