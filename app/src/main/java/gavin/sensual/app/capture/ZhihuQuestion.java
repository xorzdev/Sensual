package gavin.sensual.app.capture;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/10
 */
public class ZhihuQuestion implements Target {

    private Long id;
    private String title;
    private String image;

    public ZhihuQuestion(Long id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    @Override
    public Long getId() {
        return id;
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
