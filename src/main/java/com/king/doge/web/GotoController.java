package com.king.doge.web;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现页面跳转
 * Created by zhuru on 2019/1/8.
 */
@Controller
public class GotoController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/index")
    public String index1() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/user")
    public String user() {
        return "user";
    }

    @RequestMapping("/article")
    public String article() {
        return "article";
    }

    @RequestMapping("/picture")
    public String picture() {
        return "picture";
    }

    @RequestMapping("/file_upload")
    public String file_upload() {
        return "file_upload";
    }

    @RequestMapping("/setSession")
    @ResponseBody
    public void setSession(HttpServletRequest request) {
        request.getSession().setAttribute("hello", request.getRequestURL());
    }

    @RequestMapping("/getSession")
    @ResponseBody
    public String getSession(HttpServletRequest request) {
        Object hello = request.getSession().getAttribute("hello");
        return hello == null ? "null" : hello.toString();
    }

    @RequestMapping("/hello")
    @Cacheable(value="helloCache")
    @ResponseBody
    public String hello(String name) {
        System.out.println("没有走缓存！");
        return "hello "+name;
    }
}
