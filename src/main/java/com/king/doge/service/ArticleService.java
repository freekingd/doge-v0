package com.king.doge.service;

import com.king.doge.model.Article;
import com.king.doge.utiles.PageResult;

import java.util.Date;
import java.util.Map;

/**
 * 富文本编辑
 * Created by zhuru on 2019/1/10.
 */
public interface ArticleService {

    /**
     * 初始化查询列表
     * @param params
     * @return
     */
    PageResult findArticlesInit(Map<String, Object> params);

    /**
     * @param params
     * @param startTime
     * @param endTime
     * @return
     */
    PageResult findArticles(Map<String, Object> params, Date startTime, Date endTime);

    /**
     * 通过id获取记录
     * @param id
     * @return
     */
    Article findById(Long id);

    /**
     * 保存
     * @param article
     * @return
     */
    Article save(Article article);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    boolean deleteBatch(Long[] ids);

    int update(Article article);
}
