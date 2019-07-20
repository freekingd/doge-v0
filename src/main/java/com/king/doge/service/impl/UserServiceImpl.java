package com.king.doge.service.impl;

import com.king.doge.model.User;
import com.king.doge.repository.UserRepository;
import com.king.doge.service.UserService;
import com.king.doge.utiles.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * 用户管理
 * Created by zhuru on 2019/1/9.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User updateTokenAndLogin(String userName, String password) {
        User user = userRepository.findByUserNameAndPassword(userName, MD5Util.MD5Encode(password, "UTF-8"));
        if (user != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getId());
            if (userRepository.updateUserToken(token, user.getId()) > 0) {
                //返回数据时带上token
                user.setUserToken(token);
                return user;
            }
        }
        return null;
    }

    private String getNewToken(String sessionId, Long userId) {
        String src = sessionId + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    @Override
    @Cacheable(value = "allUsersCache", key = "'page_' + #param.get('page') + '_' + #param.get('limit')",
            unless = "#result.totalCount == 0"
    )
    public PageResult findUsers(Map<String, Object> param) {
        Page<User> page = userRepository.findAll(PageUtil.getPageable(param));
        return new PageResult(page.getContent(), page.getNumber() + 1, page.getTotalElements(), page.getSize());
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Caching(
            put = {@CachePut(value = "userCache", key = "#user.id")},
            evict = {@CacheEvict(value = "allUsersCache", allEntries = true)}
    )
    public User save(User user) {
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
        user.setCreateTime(new Date());
        return userRepository.save(user);
    }

    @Override
    @Cacheable(value = "userCache", key = "#id")
    public User findById(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Override
    public int updatePassword(User user) {
        return userRepository.updatePassword(MD5Util.MD5Encode(user.getPassword(), "UTF-8"), user.getId());
    }

    @Override
    @CacheEvict(value = "allUsersCache", allEntries = true)
    public boolean deleteBatch(Long[] ids) {
        try{
            userRepository.deleteAll(ids);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @CacheEvict(value = "allUsersCache", allEntries = true)
    public int importUsersByExcelFile(File file) {
        XSSFSheet xssfSheet = null;
        try {
            //读取file对象并转换为XSSFSheet类型对象进行处理
            xssfSheet = PoiUtil.getXSSFSheet(file);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        List<User> users = new ArrayList<>();
        //第一行是表头因此默认从第二行读取
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            //按行读取数据
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                //实体转换
                User user = convertXSSFRowToUser(xssfRow);
                //用户验证 已存在或者为空则不进行insert操作
                if (!StringUtils.isEmpty(user.getUserName()) && !StringUtils.isEmpty(user.getPassword()) && findByUserName(user.getUserName()) == null) {
                    users.add(user);
                }
            }
        }
        //判空
        if (!CollectionUtils.isEmpty(users)) {
            //Users用户列表不为空则执行批量添加sql
            try{
                userRepository.saveAll(users);
            } catch(Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 1;
    }

    /**
     * 方法抽取
     * 将解析的列转换为User对象
     *
     * @param xssfRow
     * @return
     */
    private User convertXSSFRowToUser(XSSFRow xssfRow) {
        User User = new User();
        //用户名
        XSSFCell userName = xssfRow.getCell(0);
        //密码
        XSSFCell orinalPassword = xssfRow.getCell(1);
        //设置用户名
        if (!StringUtils.isEmpty(userName)) {
            User.setUserName(PoiUtil.getValue(userName));
        }
        //对读取的密码进行加密并设置到User对象中
        if (!StringUtils.isEmpty(orinalPassword)) {
            User.setPassword(MD5Util.MD5Encode(PoiUtil.getValue(orinalPassword), "UTF-8"));
        }
        return User;
    }

    @Override
    public List<User> getUsersForExport() {
        return userRepository.findAll();
    }

    @Override
    public User findByUserToken(String token) {
        return userRepository.findByUserToken(token);
    }
}
