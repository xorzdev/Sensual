package gavin.sensual.test.multi;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/6/3
 */
public class Image {

    private String path;
    private Long parentId;
    private String parent;
    private int count;
    private boolean checked;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
