package gavin.sensual.app.base;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import gavin.sensual.util.ImageLoader;

public class Image implements Serializable {

    private String id;
    private String time;
    private String url;

    private int width;
    private int height;
    private boolean error;

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public boolean isError() {
        return error;
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

    public static Image newImage(Fragment fragment, String url) {
        Image image = new Image();
        image.url = url;
        try {
            Bitmap bm = ImageLoader.getBitmap(fragment, url);
            image.setWidth(bm.getWidth());
            image.setHeight(bm.getHeight());
        } catch (InterruptedException | ExecutionException e) {
            try {
                String fixUrl = url.replace("jpg", "jpeg").replace("JPG", "jpeg");
                Bitmap bm = ImageLoader.getBitmap(fragment, fixUrl);
                image.setUrl(fixUrl);
                image.setWidth(bm.getWidth());
                image.setHeight(bm.getHeight());
            } catch  (InterruptedException | ExecutionException e2) {
                image.setWidth(500);
                image.setHeight(500);
                image.error = true;
            }
        }
        return image;
    }
}
