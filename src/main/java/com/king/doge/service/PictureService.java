package com.king.doge.service;

import com.king.doge.model.Picture;
import com.king.doge.utiles.PageResult;

import java.util.Map;

/**
 * 图片管理
 * Created by zhuru on 2019/1/10.
 */
public interface PictureService {

    /**
     * 查找所有图片
     * @param param
     * @return
     */
    PageResult findPictures(Map<String, Object> param);

    /**
     * 保存图片
     * @param picture
     * @return
     */
    Picture save(Picture picture);

    /**
     * 删除图片
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 批量删除图片
     * @param ids
     * @return
     */
    boolean deleteBatch(Long[] ids);

    /**
     * 通过id获取图片
     * @param id
     * @return
     */
    Picture findById(Long id);

    /**
     * 批量保存图片
     * @param pictures
     * @return
     */
    boolean saveBatch(Picture[] pictures);
}
