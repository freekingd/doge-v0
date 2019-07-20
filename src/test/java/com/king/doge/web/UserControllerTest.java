package com.king.doge.web;

import com.alibaba.fastjson.JSONObject;
import com.king.doge.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * ${description}
 * Created by zhuru on 2019/1/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void login() throws Exception {
        User user = new User("doge", "do");
        String requestParam = JSONObject.toJSONString(user);
        mockMvc.perform(post("/users/login")
                .header("token", "hello")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestParam))
                .andDo(print());
    }

    @Test
    public void list() throws Exception {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("page", "1");
        param.add("limit", "6");
        String result = mockMvc.perform(get("/users/list")
                .params(param))
                .andReturn().getResponse().getContentAsString();
        System.out.println("-----------" + result + "-----------");

    }

    @Test
    public void addUser() throws Exception {
        User user = new User("admin", "123456");
        String requestParam = JSONObject.toJSONString(user);
        mockMvc.perform(post("/users/add")
                .header("token", "e3cf5c01294fb904f8ca62644ccb76a8")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestParam))
                .andDo(print());
    }

    @Test
    public void editUser() throws Exception {
        User user = new User("admin", "123456");
        user.setId(22l);
        String requestParam = JSONObject.toJSONString(user);
        mockMvc.perform(post("/users/edit")
                .header("token", "59d25d89039d90b405cce90c18dd9369")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestParam))
                .andDo(print());

    }
}