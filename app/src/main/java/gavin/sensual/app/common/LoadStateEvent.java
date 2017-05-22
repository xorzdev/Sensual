package gavin.sensual.app.common;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/22
 */
public class LoadStateEvent {

    public static final int STATE_NONE = 0;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_ERROR = -1;

    public int requestCode, state;

    public LoadStateEvent(int requestCode, int state) {
        this.requestCode = requestCode;
        this.state = state;
    }
}
