package com.king.doge.service.impl;

import com.king.doge.model.Article;
import com.king.doge.repository.ArticleRepository;
import com.king.doge.service.ArticleService;
import com.king.doge.utiles.PageResult;
import com.king.doge.utiles.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 富文本编辑
 * Created by zhuru on 2019/1/10.
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public PageResult findArticlesInit(Map<String, Object> params) {
        Page<Article> page = articleRepository.findAll(PageUtil.getPageable(params));
        return new PageResult(page.getContent(), page.getNumber() + 1, page.getTotalElements(), page.getSize());
    }

    @Override
    public PageResult findArticles(Map<String, Object> params, Date startTime, Date endTime) {
        Page<Article> page = articleRepository.findByArticleTitleLikeAndCreateTimeBetween
                ("%" + params.get("keyword").toString() + "%", startTime, endTime, PageUtil.getPageable(params));
        return new PageResult(page.getContent(), page.getNumber() + 1, page.getTotalElements(), page.getSize());
    }

    @Override
    public Article findById(Long id) {
        Optional<Article> byId = articleRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Override
    public Article save(Article article) {
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);
        return articleRepository.save(article);
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        try{
            articleRepository.deleteBatch(ids);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int update(Article article) {
        return articleRepository.update(article.getArticleTitle(), article.getArticleContent(),
                new Date(), article.getId());
    }
}
