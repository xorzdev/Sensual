package gavin.sensual.test.multi;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/6/5
 */
public class ImageCheckedChangedEvent {

    public String path;
    public boolean isChecked;

    public ImageCheckedChangedEvent(String path, boolean isChecked) {
        this.path = path;
        this.isChecked = isChecked;
    }
}
