package com.king.doge.config;

import com.king.doge.web.filter.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 注册过滤登陆的Filter
 * Created by zhuru on 2019/4/22.
 */
@Configuration
public class WebConfiguration {
    public static final String LOGIN_KEY = "LOGIN_SESSION_KEY";
    public static final String LOGIN_USER = "LOGIN_SESSION_USER";

    @Bean(name = "sessionFilter")
    public Filter sessionFilter() {
        return new SessionFilter();
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("sessionFilter");
        registration.setOrder(1);
        return registration;
    }
}