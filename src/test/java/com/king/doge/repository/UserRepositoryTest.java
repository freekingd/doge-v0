package com.king.doge.repository;

import com.king.doge.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * ${description}
 * Created by zhuru on 2019/1/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Resource
    private UserRepository userRepository;

    Pageable pageable = PageRequest.of(1, 10,new Sort(Sort.Direction.DESC, "id"));
    
    //查询用户列表
    @Test
    public void findAll() {
        Page<User> page = userRepository.findAll(pageable);
        Map map = new HashMap();
        map.put("list", page.getContent());
        map.put("currentPage", page.getNumber());
        map.put("totalPage", page.getTotalPages());
        map.put("totalCount", page.getTotalElements());
        System.out.println("-----------" + map + "-----------");
    }

    //根据用户名和密码获取用户记录
    @Test
    public void findByUserNameAndPassword() {
        User user = userRepository.findByUserNameAndPassword("doge12", "so");
        System.out.println("-----------" + user + "-----------");
    }

    // 根据token获取用户记录
    @Test
    public void findByUserToken() {
        User user = userRepository.findByUserToken("do12");
        System.out.println("-----------" + user + "-----------");
    }

    // 更新用户token
    @Test
    public void updateUserToken() {
        int i = userRepository.updateUserToken("new", 12l);
        System.out.println("-----------" + i + "-----------");
        
    }

    // 批量新增用户
    @Test
    public void saveAll() {
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 25; i++) {
            User user = new User();
            user.setUserName("doge" + i);
            user.setPassword("doo");
            users.add(user);
        }
        List<User> users1 = userRepository.saveAll(users);
        System.out.println("-----------" + users1 + "-----------");
    }

    // 更新用户密码
    @Test
    public void updatePassword() {
        int hello = userRepository.updatePassword("hello", 25l);
        System.out.println("-----------" + hello + "-----------");

    }

    // 根据用户名获取用户
    @Test
    public void findByUserName() {
        User user = userRepository.findByUserName("doge1");
        System.out.println("-----------" + user + "-----------");
        Optional<User> byId = userRepository.findById(211l);
        System.out.println("-----------" + byId.isPresent() + "-----------");
        System.out.println("-----------" + byId.get() + "-----------");
    }

    // 批量删除用户
    @Test
    public void deleteAll() {
        List<Long> users = new ArrayList<>();
        for(int i = 1; i < 25; i++) {
            users.add(Long.valueOf(i));
        }
        Long[] ids = new Long[30];
        users.toArray(ids);
        userRepository.deleteAll(ids);
    }
}