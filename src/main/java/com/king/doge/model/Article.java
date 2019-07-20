package com.king.doge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.king.doge.comm.Constants;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 富文本
 * Created by zhuru on 2019/1/9.
 */
@Entity
@Where(clause = "is_deleted=" + Constants.DELETE_FLAG_FALSE)
public class Article implements Serializable {

    @Id
    @GeneratedValue(generator = "seq_article")
    private Long id; // 主键

    @Column(name = "article_title", nullable = false, length = 50)
    private String articleTitle; //文章标题

    @Column(name = "article_content", nullable = false)
    private String articleContent; // 文章内容

    @Column(name = "add_name", nullable = false, length = 32)
    private String addName; // 添加人姓名

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime; // 最近更新时间

    @Column(name = "is_deleted")
    private int isDeleted;

    public Article() {
    }

    public Article(String articleTitle, String articleContent, String addName) {
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.addName = addName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getAddName() {
        return addName;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", articleTitle='" + articleTitle + '\'' +
                ", articleContent='" + articleContent + '\'' +
                ", addName='" + addName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
