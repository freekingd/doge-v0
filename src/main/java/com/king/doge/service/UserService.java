package com.king.doge.service;

import com.king.doge.model.User;
import com.king.doge.utiles.PageResult;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuru on 2019/1/9.
 */
public interface UserService {

    /**
     * 更新token，登陆
     * @param userName
     * @param password
     * @return
     */
    User updateTokenAndLogin(String userName, String password);

    /**
     * 查找用户列表
     * @param param
     * @return
     */
    PageResult findUsers(Map<String, Object> param);

    /**
     * 通过用户名查找用户
     * @param userName 
     * @return
     */
    User findByUserName(String userName);

    /**
     * 新增用户
     * @param user
     * @return
     */
    User save(User user);

    /**
     * 通过id获取用户
     * @param id
     * @return
     */
    User findById(Long id);

    /**
     * 更新用户密码
     * @param tempUser
     * @return
     */
    int updatePassword(User tempUser);

    /**
     * 批量删除用户记录
     * @param ids
     */
    boolean deleteBatch(Long[] ids);

    /**
     * 通过excel导入用户
     * @param file
     * @return
     */
    int importUsersByExcelFile(File file);

    /**
     * 获取用户列表
     * @return
     */
    List<User> getUsersForExport();

    /**
     * 通过token查找用户
     * @param token
     * @return
     */
    User findByUserToken(String token);
}
