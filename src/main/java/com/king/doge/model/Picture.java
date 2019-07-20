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
 * 图片
 * Created by zhuru on 2019/1/9.
 */
@Entity
@Where(clause = "is_deleted=" + Constants.DELETE_FLAG_FALSE)
public class Picture implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "seq_picture")
    private Long id;

    /**
     * 图片所在路径
     */
    @Column(nullable = false)
    private String path;

    /**
     * 图片的备注
     */
    @Column(nullable = false, length = 100)
    private String remark;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 判断是否逻辑删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;

    public Picture() {
    }

    public Picture(String path, String remark) {
        this.path = path;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
