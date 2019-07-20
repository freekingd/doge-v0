package com.king.doge.repository;

import com.king.doge.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 富文本编辑
 * Created by zhuru on 2019/1/10.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 删除单篇
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Article a set a.isDeleted = 1 where a.id = ?1")
    int deleteOne(Long id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Article a set a.isDeleted = 1 where a.id in ?1")
    int deleteBatch(Long[] ids);

    /**
     * 更新操作
     * @param articleTitle
     * @param articleContent
     * @param updateTime
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Article a set a.articleTitle= ?1, a.articleContent= ?2," +
            " a.updateTime = ?3 where a.id = ?4 and a.isDeleted = 0")
    int update(String articleTitle, String articleContent, Date updateTime, Long id);

    Page<Article> findByArticleTitleLikeAndCreateTimeBetween(String articleTitle, Date startTime, Date endTime, Pageable pageable);

}
