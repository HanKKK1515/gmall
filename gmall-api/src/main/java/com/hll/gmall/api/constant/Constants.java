package com.hll.gmall.api.constant;

/**
 * 普通常量定义
 */
public class Constants {
    /**
     * FastDFS 服务器地址
     */
    public static String TRACKER_SERVER_ADDRESS = "http://192.168.142.101";

    /**
     * Elasticsearch indices
     */
    public static String ES_INDICES = "gmallpms";

    /**
     * Elasticsearch type
     */
    public static String ES_TYPE = "pmsSkuInfo";

    public static String PROJECT_NAME = "gmall";

    /**
     * 微博登录 code 请求地址
     */
    public static String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";

    /**
     * 微博登录成功回调地址
     */
    public static String REDIRECT_URL = "http://localhost:8085/vLogin.html";

    /**
     * 微博登录获取 access token url
     */
    public static String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

    /**
     * 微博登录获取 token info url
     */
    public static String TOKEN_INFO_URL = "https://api.weibo.com/oauth2/get_token_info";

    /**
     * 微博登录获取 users show url
     */
    public static String USERS_SHOW_URL = "https://api.weibo.com/2/users/show.json";

    /**
     * 微博登录 client id
     */
    public static String CLIENT_ID = "381527720";

    /**
     * 微博登录 client secret
     */
    public static String CLIENT_SECRET = "428678e1dbf5593628e99f2de321a747";

}
