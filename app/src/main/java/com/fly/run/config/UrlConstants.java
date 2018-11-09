package com.fly.run.config;

public class UrlConstants {
    /**
     * home IP
     * */
//    public static String HTTP_ROOT = "http://192.168.31.90:9090/";
    /**
     * 公司 IP
     */
    public static String HTTP_ROOT = "http://172.16.0.173:9090/";
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
     * 压缩图片下载
     */
    public static String HTTP_DOWNLOAD_FILE_SCALE = HTTP_ROOT + "download_scale_file?filename=%s";

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
    public static String HTTP_USER_REGISTER = HTTP_ROOT + "account/registerUser";

    /**
     * 用户编辑
     */
    public static String HTTP_USER_EDIT = HTTP_ROOT + "account/edit";

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
     * 查询跑友圈（好友或已关注的人）
     */
    public static String HTTP_CIRCLE_QUERY = HTTP_ROOT + "circle/query";

    /**
     * 查询跑友圈（搜索其他跑者）
     */
    public static String HTTP_CIRCLE_SEARCH_QUERY = HTTP_ROOT + "circle/querySearch";

    /**
     * 查询我发的跑友圈
     */
    public static String HTTP_CIRCLE_QUERY_BY_ID = HTTP_ROOT + "circle/query_by_id";

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

    /**
     * 查询SysFITPlan
     */
    public static String HTTP_FIT_PLAN_QUERY = HTTP_ROOT + "fit/query_fit_plan";

    public static final String BASE_URL_V3_5050 = "http://www.melinked.com:5050/";
    public static final String HTTP_QINIU_UPTOKEN = BASE_URL_V3_5050 + "/qiniu/api/getUploadToken";
    public static final String HTTP_QINIU_DATA_IM_FILE = "http://imfile.melinked.com/"; // 七牛IM文件下载地址
    public static final String BASE_QINIIU_SCALE = "?imageView2/2/w/200";
    public static final String BASE_QINIU_Thumbnail = "?vframe/jpg/offset/0"; // 七牛获取视频第一帧图片
    public static final String BASE_QINIU_Thumbnail_2 = "?vframe/jpg/offset/"; // 七牛获取视频某一帧图片

    /**
     * 查询跑友圈某条记录的回复
     */
    public static String HTTP_CIRCLE_REPLY_QUERY = HTTP_ROOT + "circle/query_reply";

    /**
     * 添加回复
     */
    public static String HTTP_CIRCLE_REPLY_INSERT = HTTP_ROOT + "circle/insert_reply";

    /**
     * 点赞
     */
    public static String HTTP_CIRCLE_LIKE_INSERT = HTTP_ROOT + "circle/like/insert";

    /**
     * 查询点赞数量
     */
    public static String HTTP_CIRCLE_LIKE_QUERY = HTTP_ROOT + "circle/like/query";

    /**
     * 分享
     */
    public static String HTTP_CIRCLE_SHARE_INSERT = HTTP_ROOT + "circle/share/insert";

    /**
     * 查询分享数量
     */
    public static String HTTP_CIRCLE_SHARE_QUERY = HTTP_ROOT + "circle/share/query";
}
