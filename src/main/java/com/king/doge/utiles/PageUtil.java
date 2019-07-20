package com.king.doge.utiles;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * Created by zhuru on 2018/12/17.
 */
public class PageUtil {

    public static Pageable getPageable(Map<String, Object> param) {
        int page = Integer.parseInt(param.get("page").toString()) - 1;
        int size = Integer.parseInt(param.get("limit").toString());
        return PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "id"));
    }
}
