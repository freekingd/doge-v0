package com.king.doge.service.impl;

import com.king.doge.model.Article;
import com.king.doge.service.ArticleService;
import com.king.doge.utiles.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ${description}
 * Created by zhuru on 2019/1/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceImplTest {

    @Resource
    private ArticleService articleService;

    @Test
    public void findArticles() {
        Date endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.add(Calendar.DATE, -1);
        Date startTime = calendar.getTime();
        System.out.println("-----------" + startTime + "-----------");

        Map<String, Object> params = new HashMap<>();
        params.put("keyword", "%%");
        params.put("page", "1");
        params.put("limit", "6");
        PageResult articles = articleService.findArticles(params, startTime, endTime);
        System.out.println("-----------" + articles + "-----------");


    }

    @Test
    public void save() {
        Article article = new Article("hello_test", "world_test", "doge_test");
        article.setId(1l);
        Article save = articleService.save(article);
        System.out.println("-----------" + save + "-----------");
    }

    @Test
    public void update() {
        Article article = new Article("hello1", "afj", "doge12");
        article.setId(2l);
        int i = articleService.update(article);
        System.out.println("-----------" + i + "-----------");

    }
}