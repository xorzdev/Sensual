package gavin.sensual.app.main;

/**
 * 左侧菜单开关事件
 *
 * @author gavin.xiong 2016/12/5  2016/12/5
 */
public class ShowHideFragmentEvent {

    public int position;
    public long delay;

    public ShowHideFragmentEvent(int position) {
        this.position = position;
    }

    public ShowHideFragmentEvent(int position, long delay) {
        this.position = position;
        this.delay = delay;
    }
}
