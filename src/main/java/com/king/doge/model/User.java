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
 * 用户实体
 * Created by zhuru on 2019/1/9.
 */
@Entity
@Where(clause = "is_deleted=" + Constants.DELETE_FLAG_FALSE)
public class User implements Serializable {

    /**
     *  主键
     */
    @Id
    @GeneratedValue(generator = "seq_user")
    private Long id;
    /**
     *  用户名
     */
    @Column(name = "user_name", nullable = false, length = 32, unique = true)
    private String userName;
    /**
     *  密码
     */
    @Column(nullable = false, length = 32)
    private String password;
    /**
     *  用户token
     */
    @Column(name = "user_token", length = 32)
    private String userToken;
    /**
     *  逻辑删除   0未删除    1已删除
     */
    @Column(name = "is_deleted")
    private int isDeleted;
    /**
     *  创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    public User() {}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AdminUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userToken='" + userToken + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                '}';
    }
}
