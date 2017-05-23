package gavin.sensual.app.common;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/22
 */
public class NewImageEvent {

    public int requestCode;
    public Image image;

    public NewImageEvent(int requestCode, Image image) {
        this.requestCode = requestCode;
        this.image = image;
    }
}
