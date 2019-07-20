package com.king.doge.comm;

/**
 * 常量
 * Created by zhuru on 2019/1/9.
 */
public class Constants {

    public static final int DELETE_FLAG_FALSE = 0;
    public static final int DELETE_FLAG_TRUE = 1;
    public static final int RESULT_CODE_SUCCESS = 200;  // 成功处理请求
    public static final int RESULT_CODE_BAD_REQUEST = 412;  // 请求错误
    public static final int RESULT_CODE_NOT_LOGIN = 402;  // 未登录
    public static final int RESULT_CODE_PARAM_ERROR = 406;  // 传参错误
    public static final int RESULT_CODE_CHUNK_EXIST = 298;  // 自定义code 分片已存在
    public static final int RESULT_CODE_CHUNK_NOTEXIST = 299;  // 自定义code 分片不存在
    public static final int RESULT_CODE_SERVER_ERROR = 500;  // 服务器错误

    public final static int PAGE_SIZE = 10;//默认分页条数

    //public final static String FILE_PRE_URL = "D:\\Java\\IdeaProjects\\doge\\src\\main\\resources\\static\\upload\\";//上传文件的默认url前缀，根据部署修改
    public final static String FILE_PRE_URL = "/var/artifact/upload/";//上传文件的默认url前缀，根据部署修改

    public static final String ARTICLE_CACHE_KEY = "doge:article:";//文章存储于redis的key前缀

}
