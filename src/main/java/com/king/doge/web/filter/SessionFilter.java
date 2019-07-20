package com.king.doge.web.filter;

import com.king.doge.config.WebConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhuru on 2019/4/22.
 */
public class SessionFilter implements Filter {

    protected Logger log = LoggerFactory.getLogger(SessionFilter.class);

    private static Set<String> GreenUrlSet = new HashSet<>(); // 白名单

    public void doFilter(ServletRequest srequest, ServletResponse sresponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) srequest;
        String uri = request.getRequestURI();
        sresponse.setCharacterEncoding("UTF-8");//设置响应编码格式
        sresponse.setContentType("text/html;charset=UTF-8");//设置响应编码格式
        if (uri.endsWith(".js")
                || uri.endsWith(".map")
                || uri.endsWith(".css")
                || uri.endsWith(".jpg")
                || uri.endsWith(".gif")
                || uri.endsWith(".png")
                || uri.endsWith(".ico")) {
            log.debug("security filter, pass, " + request.getRequestURI());
            filterChain.doFilter(srequest, sresponse);
            return;
        }

        System.out.println("request uri is : "+uri);
        //不处理指定的action, jsp
        if (GreenUrlSet.contains(uri) || uri.contains("/verified/")) {
            log.debug("security filter, pass, " + request.getRequestURI());
            filterChain.doFilter(srequest, sresponse);
            return;
        }
        Object id = request.getSession().getAttribute(WebConfiguration.LOGIN_KEY);
        if(StringUtils.isEmpty(id)){
            //String html = "<script type=\"text/javascript\">window.location.href=\"/toLogin\"</script>";
            //sresponse.getWriter().write(html);
            srequest.getRequestDispatcher("/login").forward(srequest, sresponse);
        }else {
            filterChain.doFilter(srequest, sresponse);
        }
    }

    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterconfig) {
        GreenUrlSet.add("/");
        GreenUrlSet.add("//fly/login");
        GreenUrlSet.add("//fly/users/login");
        GreenUrlSet.add("//fly/index");
        GreenUrlSet.add("//fly/toLogin");
        GreenUrlSet.add("//fly/users/logout");
    }
}
