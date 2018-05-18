package com.yglbs.model;

/**
 * 个人博客信息
 * @author yeelxd
 * @date 2018-03-01
 */
public class Blog {
    private Long bid;
    private String author;
    private String title;
    private String content;

    public Blog(Long bid, String author, String title, String content) {
        this.bid = bid;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "bid=" + bid +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
