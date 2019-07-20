package com.king.doge.service.impl;

import com.king.doge.model.User;
import com.king.doge.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${description}
 * Created by zhuru on 2019/1/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void save() {
        for(int i = 0; i < 21; i++) {
            User user = new User("doge" + i, "doo");
            userService.save(user);
        }
    }

    @Test
    public void updateTokenAndLogin() {
        for(int i = 0; i < 21; i++) {
            userService.updateTokenAndLogin("doge" + i, "doo");
        }
    }
}