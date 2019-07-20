package com.king.doge.service.impl;

import com.king.doge.model.Picture;
import com.king.doge.repository.PictureRepository;
import com.king.doge.service.PictureService;
import com.king.doge.utiles.PageResult;
import com.king.doge.utiles.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 图片管理
 * Created by zhuru on 2019/1/10.
 */
@Service("pictureService")
public class PictureServiceImpl implements PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    @Cacheable(value = "allPicturesCache", key = "'page_' + #param.get('page') + '_' + #param.get('limit')",
            unless = "#result.totalCount == 0"
    )
    public PageResult findPictures(Map<String, Object> param) {
        Page<Picture> page = pictureRepository.findAll(PageUtil.getPageable(param));
        return new PageResult(page.getContent(), page.getNumber() + 1, page.getTotalElements(), page.getSize());
    }

    @Override
    @Caching(
            put = {@CachePut(value = "pictureCache", key = "#picture.id")},
            evict = {@CacheEvict(value = "allPicturesCache", allEntries = true)}
    )
    public Picture save(Picture picture) {
        picture.setCreateTime(new Date());
        return pictureRepository.save(picture);
    }

    @Override
    @CacheEvict(value = "allPicturesCache", allEntries = true)
    public int deleteById(Long id) {
        return pictureRepository.deleteOne(id);
    }

    @Override
    @CacheEvict(value = "allPicturesCache", allEntries = true)
    public boolean deleteBatch(Long[] ids) {
        try{
            pictureRepository.deleteAll(ids);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Picture findById(Long id) {
        Optional<Picture> byId = pictureRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Override
    @CacheEvict(value = "allPicturesCache", allEntries = true)
    public boolean saveBatch(Picture[] pictures) {
        try{
            for(int i = 0; i < pictures.length; i++) {
                save(pictures[i]);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
