package com.king.doge.repository;

import com.king.doge.model.Article;
import com.king.doge.utiles.DateUtil;
import com.king.doge.utiles.PageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

/**
 * ${description}
 * Created by zhuru on 2019/1/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleRepositoryTest {

    @Resource
    private ArticleRepository articleRepository;

    @Test
    public void findByArticleTitleLikeAndCreateTimeBetween() throws ParseException {
        Page<Article> page = articleRepository.findByArticleTitleLikeAndCreateTimeBetween(
                "hello", null, new Date(), PageUtil.getPageable(new HashMap<>()));
        System.out.println("-----------" + DateUtil.toDate("2019-01-09 12:00:08", 0) + "-----------");


    }
}