package com.king.doge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 注册自定义分解器
 * Created by zhuru on 2019/1/10.
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    //@Override
    //protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    //    super.addArgumentResolvers(argumentResolvers);
    //    argumentResolvers.add(new TokenToUserMethodArgumentResolver());
    //}

    /**
     * 使用WebMvcConfigurationSupport后，springboot关于静态资源的自动配置将失效
     * 此处采用spring mvc 的资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
                .addResourceLocations("/var/artifact/upload/");
        super.addResourceHandlers(registry);
    }
}
