package gavin.sensual.app.common;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/22
 */
public class BigImagePopEvent {

    public int requestCode;
    public int position = -1;

    public BigImagePopEvent(int requestCode, int position) {
        this.requestCode = requestCode;
        this.position = position;
    }
}
