package com.king.doge.web.handler;

import com.king.doge.config.ApplicationContextProvider;
import com.king.doge.model.User;
import com.king.doge.service.UserService;
import com.king.doge.web.annotation.TokenToUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by zhuru on 2018/12/17.
 */
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public TokenToUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (parameter.getParameterAnnotation(TokenToUser.class) instanceof TokenToUser) {
            User user = null;
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token)) {
                UserService userService = (UserService) ApplicationContextProvider.getBean("userService");
                user = userService.findByUserToken(token);
            }
            return user;
        }
        return null;
    }

}
