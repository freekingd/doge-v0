package com.king.doge.repository;

import com.king.doge.model.Picture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ${description}
 * Created by zhuru on 2019/1/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureRepositoryTest {

    @Resource
    private PictureRepository pictureRepository;

    // 批量插入
    @Test
    public void saveAll() {
        List<Picture> lists = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            Picture picture = new Picture("/hello" + i, "world");
            lists.add(picture);
        }
        List<Picture> pictures = pictureRepository.saveAll(lists);
        System.out.println("-----------" + pictures.size() + "-----------");

    }
}