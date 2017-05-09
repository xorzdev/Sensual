package gavin.sensual.app.gank;

import java.io.Serializable;
import java.util.List;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/9
 */
public class SharedPager<T> implements Serializable {

    public List<T> list;
    public int index;
    public int limit;
    public int no;
}
