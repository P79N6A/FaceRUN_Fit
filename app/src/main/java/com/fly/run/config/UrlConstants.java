package com.fly.run.config;

public class UrlConstants {
    /**
     * home IP
     * */
//    public static String HTTP_ROOT = "http://192.168.31.90:9090/";
    /**
     * 公司 IP
     */
    public static String HTTP_ROOT = "http://172.16.0.138:9090/";
    /**
     * 阿里云
     */
//    public static String HTTP_ROOT = "http://120.78.81.9/";

    /**
     * 单文件上传（图片）
     */
    public static String HTTP_UPLOAD_SINGLE_IMAGE = HTTP_ROOT + "upload";

    /**
     * 多文件上传（图片）
     */
    public static String HTTP_UPLOAD_IMAGES = HTTP_ROOT + "batch/upload";

    /**
     * 文件下载
     */
    public static String HTTP_DOWNLOAD_FILE = HTTP_ROOT + "download_file";

    /**
     * 图片下载
     */
    public static String HTTP_DOWNLOAD_FILE_2 = HTTP_ROOT + "download_file?filename=%s";

    /**
     * 设置在线用户的位置
     */
    public static String HTTP_USER_ONLINE = HTTP_ROOT + "facerunner/setOnLineUserID";

    /**
     * 设置用户运动结束状态
     */
    public static String HTTP_USER_OFFLINE = HTTP_ROOT + "facerunner/setOffLineUserID";

    /**
     * 用户注册
     */
    public static String HTTP_USER_REGISTER = HTTP_ROOT + "user/registerUser";

    /**
     * 用户登录
     */
    public static String HTTP_USER_LOGIN = HTTP_ROOT + "account/login";

    /**
     * 用户登出
     */
    public static String HTTP_USER_LOGIN_OUT = HTTP_ROOT + "account/logout";

    /**
     * 用户更新信息
     */
    public static String HTTP_USER_UPDATE = HTTP_ROOT + "user/updateUser";

    /**
     * 查询用户列表
     */
    public static String HTTP_USER_QUERY_LIST = HTTP_ROOT + "user/queryUserList";

    /**
     * 查询用户信息
     */
    public static String HTTP_USER_QUERY = HTTP_ROOT + "user/queryUser";

    /**
     * 查询用户信息
     * @param account
     */
    public static String HTTP_USER_QUERY_BY_ACCOUNT = HTTP_ROOT + "account/select_by_account";

    /**
     * 查询用户信息
     *
     * @param account_id
     */
    public static String HTTP_USER_QUERY_BY_ACCOUNT_ID = HTTP_ROOT + "account/select_by_id";

    /**
     * 查询跑友圈
     */
    public static String HTTP_CIRCLE_QUERY = HTTP_ROOT + "circle/query";

    /**
     * 插入跑友圈
     */
    public static String HTTP_CIRCLE_INSERT = HTTP_ROOT + "circle/insert";

    /**
     * 删除跑友圈
     */
    public static String HTTP_CIRCLE_DELETE = HTTP_ROOT + "circle/delete";

    /**
     * 查询FIT
     */
    public static String HTTP_FIT_QUERY = HTTP_ROOT + "fit/query";
}
