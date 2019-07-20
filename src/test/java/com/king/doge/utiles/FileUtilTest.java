package com.king.doge.utiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * ${description}
 * Created by zhuru on 2019/1/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUtilTest {


    @Test
    public void createFile() throws IOException {
        String path = "D:\\imssage\\ted\\12\\12.jpg";
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
    }

    @Test
    public void test() {
        String s1 = "qq";
        String s2 = "qq";
        System.out.println(s1==s2);
        System.out.println("-----------"  + "-----------");

    }
}