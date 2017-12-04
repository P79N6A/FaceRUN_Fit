package com.fly.run.config;

public class UrlConstants {
//    192.168.43.96 oppo r9
//    192.168.31.90 家
//    172.16.0.12 公司
//    http://flyrun.ittun.com
    /**
     * home IP
     * */
//    public static String HTTP_ROOT = "http://192.168.31.90:9000/";
    /**
     * 公司 IP
     */
    public static String HTTP_ROOT = "http://172.16.0.138:9090/";
    /**
     * OPPO R9
     */
//    public static String HTTP_ROOT = "http://flyrun.ittun.com/";

    /**
     * 图片根路径
     */
    public static String HTTP_SHOW_IMAGE = HTTP_ROOT + "UpLoadPath/FILE/";

    /**
     * 文件上传（图片）
     */
    public static String HTTP_UPLOAD_IMAGE = HTTP_ROOT + "upload/uploadFile";

    /**
     * 文件下载
     */
    public static String HTTP_DOWNLOAD_FILE = HTTP_ROOT + "download/downloadFile";

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
     * @param account_id
     */
    public static String HTTP_USER_QUERY_BY_ACCOUNT_ID = HTTP_ROOT + "account/select_by_id";

    /**
     * 保存活动
     */
    public static String HTTP_ACTIVE_SAVE = HTTP_ROOT + "active/saveActiveData";

    /**
     * 活动数据查询列表
     */
    public static String HTTP_ACTIVE_QUERY_ALL = HTTP_ROOT + "active/queryActiveDataList";

    /**
     * 查询个人活动列表
     */
    public static String HTTP_ACTIVE_QUERY_USER = HTTP_ROOT + "active/queryUserActives";

    /**
     * 删除活动
     */
    public static String HTTP_ACTIVE_DELETE = HTTP_ROOT + "active/delActiveData";

    /**
     * 查询跑友圈
     */
    public static String HTTP_QUERY_CIRCLE_RUN = HTTP_ROOT + "run/list_by_account";
}
