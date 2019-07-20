package com.king.doge.repository;

import com.king.doge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户管理
 * Created by zhuru on 2019/1/9.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户id更新token
     * @param newToken
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update User u set u.userToken = ?1 where u.id = ?2 and u.isDeleted = 0")
    int updateUserToken(String newToken, Long id);

    /**
     * 更新用户密码，同时刷新token
     * @param newPassword
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1, u.userToken = '' where u.id = ?2 and u.isDeleted = 0")
    int updatePassword(String newPassword, Long id);

    /**
     * 批量删除用户（逻辑删除）
     * @param ids
     * @return
     */
    @Transactional
    @Modifying
    @Query("update User u set u.isDeleted = 1 where u.id in ?1")
    int deleteAll(Long[] ids);

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update User u set u.isDeleted = 1 where u.id = ?1")
    int deleteOne(Long id);

    User findByUserNameAndPassword(String userName, String password);

    User findByUserToken(String userToken);

    User findByUserName(String userName);

}
