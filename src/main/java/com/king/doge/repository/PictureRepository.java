package com.king.doge.repository;

import com.king.doge.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图片管理
 * Created by zhuru on 2019/1/10.
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {

    /**
     * 删除单张图片
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Picture p set p.isDeleted = 1 where p.id = ?1")
    int deleteOne(Long id);

    /**
     * 批量删除图片
     * @param ids
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Picture p set p.isDeleted = 1 where p.id in ?1")
    int deleteAll(Long[] ids);

}
