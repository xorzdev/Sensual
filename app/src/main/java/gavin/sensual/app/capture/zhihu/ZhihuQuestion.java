package gavin.sensual.app.capture.zhihu;

import gavin.sensual.app.capture.Target;

/**
 * 知乎看图
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuQuestion implements Target {

    private Long id;
    private String title;
    private String image;
    private int type; // 0:question 1:collection

    public ZhihuQuestion(Long id, int type, String title, String image) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.image = image;
    }

    @Override
    public Long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImage() {
        return image;
    }
}
