package cn.exrick.manager.pojo;

public class TbNews implements java.io.Serializable {
    private Integer id;

    private String title;

    private Long tday;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Long getTday() {
        return tday;
    }

    public void setTday(Long tday) {
        this.tday = tday;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}