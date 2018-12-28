package com.fly.run.httptask;

import android.text.TextUtils;

import com.fly.run.app.App;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.CircleReply;
import com.fly.run.bean.NearByBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.PhoneUtil;
import com.fly.run.utils.ToastUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by kongwei on 2017/3/10.
 */

public class HttpTaskUtil {

    private ResultListener resultListener;

    private HttpTaskUtil mInstance;

    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    public static final OkHttpClient client = new OkHttpClient();
//            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
//            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
//            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
//            .build();

    public static final MediaType JSONMediaType = MediaType.parse("application/json; charset=utf-8");

    public static String post(String url, String json) throws IOException {
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//设置写的超时时间
        client.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        RequestBody body = RequestBody.create(JSONMediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

//    public HttpTaskUtil getInstance() {
//        if (mInstance == null) {
//            synchronized (HttpTaskUtil.class) {
//                if (mInstance == null) {
//                    mInstance = new HttpTaskUtil();
//                }
//            }
//        }
//        return mInstance;
//    }

    public interface ResultListener {
        public void onResponse(String response);

        public void onFailure(Request request, Exception e);
    }

    public HttpTaskUtil setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    /**
     * 查询跑步记录
     * */
    public void queryRunDatasTask(Map<String,String> param, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(UrlConstants.HTTP_RUN_QUERY_LIST, callback != null ? callback : new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (callback != null){
                        callback.onFailure(request, e);
                    } else if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (callback != null){
                        callback.onResponse(response);
                    } else if (resultListener != null)
                        resultListener.onResponse(response);
                }
            },param);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null){
                callback.onFailure(null, new IOException("Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 同步跑步记录
     * */
    public void synchronizeRunDataTask(Map<String,String> param, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(UrlConstants.HTTP_RUN_INSERT, callback != null ? callback : new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (callback != null){
                        callback.onFailure(request, e);
                    } else if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (callback != null){
                        callback.onResponse(response);
                    } else if (resultListener != null)
                        resultListener.onResponse(response);
                }
            },param);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null){
                callback.onFailure(null, new IOException("Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 点赞
     * */
    public void InsertCircleShareTask(Map<String,String> param, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(UrlConstants.HTTP_CIRCLE_SHARE_INSERT, callback != null ? callback : new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (callback != null){
                        callback.onFailure(request, e);
                    } else if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (callback != null){
                        callback.onResponse(response);
                    } else if (resultListener != null)
                        resultListener.onResponse(response);
                }
            },param);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null){
                callback.onFailure(null, new IOException("Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 点赞
     * */
    public void InsertCircleLikeTask(Map<String,String> param, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(UrlConstants.HTTP_CIRCLE_LIKE_INSERT, callback != null ? callback : new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (callback != null){
                        callback.onFailure(request, e);
                    } else if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (callback != null){
                        callback.onResponse(response);
                    } else if (resultListener != null)
                        resultListener.onResponse(response);
                }
            },param);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null){
                callback.onFailure(null, new IOException("Server Error"));
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 查询点赞数量
     * */
    public void QueryCircleLikeTask(Map<String,String> param, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(UrlConstants.HTTP_CIRCLE_LIKE_QUERY, callback != null ? callback : new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (callback != null){
                        callback.onFailure(request, e);
                    } else if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (callback != null){
                        callback.onResponse(response);
                    } else if (resultListener != null)
                        resultListener.onResponse(response);
                }
            },param);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null){
                callback.onFailure(null, (IOException) e);
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 插入一条评论
     * */
    public void InsertCircleReplyTask(Map<String,String> param, final OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.getInstance().postAsyn(UrlConstants.HTTP_CIRCLE_REPLY_INSERT, callback != null ? callback : new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (callback != null){
                        callback.onFailure(request, e);
                    } else if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (callback != null){
                        callback.onResponse(response);
                    } else if (resultListener != null)
                        resultListener.onResponse(response);
                }
            },param);
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null){
                callback.onFailure(null, (IOException) e);
            } else if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 查询跑友圈某条记录的评论
     * */
    public void QueryCircleReplyRunTask(int pageNum, int pageSize,int reply_circle_id,int reply_root_id) {
        try {
            OkHttpClientManager.Param paramPageNum = new OkHttpClientManager.Param("pageNum", "" + pageNum);
            OkHttpClientManager.Param paramPageSize = new OkHttpClientManager.Param("pageSize", "" + pageSize);
            OkHttpClientManager.Param replyCircleId = new OkHttpClientManager.Param("reply_circle_id", "" + reply_circle_id);
            OkHttpClientManager.Param replyRootId = new OkHttpClientManager.Param("reply_root_id", "" + reply_root_id);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_CIRCLE_REPLY_QUERY, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramPageNum, paramPageSize,replyCircleId,replyRootId);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 获取上传文件的token
     * */
    public String GetUploadTokenInfoTask(Map<String,String> param) {
        String response = "";
        try {
            OkHttpClientManager.Param policyJsonStr = new OkHttpClientManager.Param("policyJsonStr", param.get("policyJsonStr"));
            OkHttpClientManager.Param bucket = new OkHttpClientManager.Param("bucket", param.get("bucket"));
            response = OkHttpClientManager.getInstance()._postAsString(UrlConstants.HTTP_QINIU_UPTOKEN,policyJsonStr,bucket);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
        return response;
    }

    public void QuerySysFitPlanTask() {
        try {
            OkHttpClientManager.Param paramAccountId = new OkHttpClientManager.Param("account_id", "" + UserInfoManager.getInstance().getAccountId());
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_FIT_PLAN_QUERY, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccountId);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    public void QueryFitTask() {
        try {
            OkHttpClientManager.getInstance()._getAsyn(UrlConstants.HTTP_FIT_QUERY, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    public void UploadSingleFileTask(File file, String fileKey) {
        try {
            OkHttpClientManager.postAsyn(UrlConstants.HTTP_UPLOAD_SINGLE_IMAGE, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show(e != null ? e.getMessage() : "onFailure 文件上传失败");
                }

                @Override
                public void onResponse(String response) {
                    ToastUtil.show(response != null ? response : "response 文件上传失败");
                }
            }, file, fileKey);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(e != null ? e.getMessage() : "catch 文件上传失败");
        }
    }

    public void UploadFilesTask(File[] files, String[] fileKeys, OkHttpClientManager.StringCallback callback) {
        try {
            OkHttpClientManager.Param paramData = new OkHttpClientManager.Param("data", "ABCD");
            OkHttpClientManager.Param paramAccountId = new OkHttpClientManager.Param("account_id", "" + UserInfoManager.getInstance().getAccountInfo().getId());
            OkHttpClientManager.postAsyn(UrlConstants.HTTP_UPLOAD_IMAGES, callback, files, fileKeys, paramAccountId, paramData);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(e != null ? e.getMessage() : "文件上传失败");
        }
    }

    public void InsertCircleRunTask(String data, String account_id) {
        try {
            OkHttpClientManager.Param paramData = new OkHttpClientManager.Param("data", data);
            OkHttpClientManager.Param paramAccountId = new OkHttpClientManager.Param("account_id", account_id);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_CIRCLE_INSERT, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramData, paramAccountId);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    public void QueryCircleByIdRunTask(int pageNum, int pageSize, String account_id) {
        try {
            OkHttpClientManager.Param paramPageNum = new OkHttpClientManager.Param("pageNum", "" + pageNum);
            OkHttpClientManager.Param paramPageSize = new OkHttpClientManager.Param("pageSize", "" + pageSize);
            OkHttpClientManager.Param paramAccountId = new OkHttpClientManager.Param("account_id", account_id);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_CIRCLE_QUERY_BY_ID, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramPageNum, paramPageSize, paramAccountId);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    public void QueryCircleRunTask(int pageNum, int pageSize) {
        try {
            OkHttpClientManager.Param paramPageNum = new OkHttpClientManager.Param("pageNum", "" + pageNum);
            OkHttpClientManager.Param paramPageSize = new OkHttpClientManager.Param("pageSize", "" + pageSize);
            OkHttpClientManager.Param userId = new OkHttpClientManager.Param("userId", "" + UserInfoManager.getInstance().getAccountId());
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_CIRCLE_QUERY, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramPageNum, paramPageSize,userId);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    public void QueryCircleSearchRunTask(int pageNum, int pageSize) {
        try {
            OkHttpClientManager.Param paramPageNum = new OkHttpClientManager.Param("pageNum", "" + pageNum);
            OkHttpClientManager.Param paramPageSize = new OkHttpClientManager.Param("pageSize", "" + pageSize);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_CIRCLE_SEARCH_QUERY, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramPageNum, paramPageSize);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 用户注册
     */
    public void RegisterTask(String account, String password) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", account);
            OkHttpClientManager.Param paramPassword = new OkHttpClientManager.Param("password", password);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_REGISTER, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show("注册失败:服务器异常，请查看网络连接状态！");
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount, paramPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户编辑
     */
    public void UserEditTask(String account, String name) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", account);
            OkHttpClientManager.Param paramName = new OkHttpClientManager.Param("name", name);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_EDIT, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show("用户编辑失败:服务器异常，请查看网络连接状态！");
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount, paramName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 编辑用户信息
     */
    public void UserEditTask(AccountBean bean) {
        try {
            String header = bean.getHeadPortrait() == null ? "" : bean.getHeadPortrait();
            String sex = bean.getSex() == null ? "" : bean.getSex();
            String desc = bean.getDescription() == null ? "" : bean.getDescription();
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", bean.getAccount());
            OkHttpClientManager.Param paramName = new OkHttpClientManager.Param("name", bean.getName());
            OkHttpClientManager.Param paramHeader = new OkHttpClientManager.Param("header", header);
            OkHttpClientManager.Param paramSex = new OkHttpClientManager.Param("sex", sex);
            OkHttpClientManager.Param paramDesc = new OkHttpClientManager.Param("desc", desc);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_EDIT, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show("用户编辑失败:服务器异常，请查看网络连接状态！");
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount, paramName, paramHeader, paramSex, paramDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoginTask(String account, String password) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", account);
            OkHttpClientManager.Param paramPassword = new OkHttpClientManager.Param("password", password);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_LOGIN, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount, paramPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateUserInfoTask(String account, String username, String sex, String age, String tag, String desc, String profession) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("Account", account);
            OkHttpClientManager.Param paramUserName = new OkHttpClientManager.Param("UserName", username);
            OkHttpClientManager.Param paramSex = new OkHttpClientManager.Param("Sex", sex);
            OkHttpClientManager.Param paramAge = new OkHttpClientManager.Param("Age", age);
            OkHttpClientManager.Param paramTag = new OkHttpClientManager.Param("Tag", tag);
            OkHttpClientManager.Param paramDescription = new OkHttpClientManager.Param("Description", desc);
            OkHttpClientManager.Param paramProfession = new OkHttpClientManager.Param("Profession", profession);

            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_UPDATE, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show("注册失败:服务器异常，请查看网络连接状态！");
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount, paramUserName, paramSex, paramAge, paramTag, paramDescription, paramProfession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QueryUserByAccountTask(String account) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", account);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_QUERY_BY_ACCOUNT, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show("注册失败:服务器异常，请查看网络连接状态！");
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QueryUserListTask() {
        try {
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_QUERY_LIST, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.show("注册失败:服务器异常，请查看网络连接状态！");
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoginOutTask(String account) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("Account", account);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_LOGIN_OUT, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (resultListener != null)
                        resultListener.onFailure(request, e);
                }

                @Override
                public void onResponse(String response) {
                    if (resultListener != null)
                        resultListener.onResponse(response);
                }
            }, paramAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void httpOnLineLocalTask(NearByBean bean, OkHttpClientManager.StringCallback callback) {
        String strUserId = "";
        String strUserName = "";
        String strUserCover = "";
        AccountBean accountBean = UserInfoManager.getInstance().getAccountInfo();
        if (accountBean != null && !TextUtils.isEmpty(accountBean.getAccount())) {
            strUserId = accountBean.getAccount();
            strUserName = accountBean.getUsername();
            strUserCover = accountBean.getHeadPortrait() == null ? "" : accountBean.getHeadPortrait();
        } else {
            strUserId = PhoneUtil.getDeviceId(App.getInstance());
            strUserName = strUserId;
            strUserCover = "";
        }
        OkHttpClientManager.Param param1 = setLocateParam("UserID", strUserId);
        OkHttpClientManager.Param param2 = setLocateParam("address", bean.getAddress());
        OkHttpClientManager.Param param3 = setLocateParam("latitude", String.valueOf(bean.getLatitude()));
        OkHttpClientManager.Param param4 = setLocateParam("longitude", String.valueOf(bean.getLongitude()));
        OkHttpClientManager.Param param5 = setLocateParam("online", "true");
        OkHttpClientManager.Param param6 = setLocateParam("UserName", strUserName);
        OkHttpClientManager.Param param7 = setLocateParam("UserCover", strUserCover);
        OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_ONLINE,
                callback, param1, param2, param3, param4, param5, param6, param7);
    }

    public void httpOffLineLocalTask(OkHttpClientManager.StringCallback callback) {
        String strUserId = "";
        AccountBean accountBean = UserInfoManager.getInstance().getAccountInfo();
        if (accountBean != null && !TextUtils.isEmpty(accountBean.getAccount())) {
            strUserId = accountBean.getAccount();
        } else {
            strUserId = PhoneUtil.getDeviceId(App.getInstance());
        }
        OkHttpClientManager.Param param1 = setLocateParam("UserID", strUserId);
        OkHttpClientManager.Param param2 = setLocateParam("online", "false");
        OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_OFFLINE,
                callback, param1, param2);
    }

    public OkHttpClientManager.Param setLocateParam(String key, String value) {
        OkHttpClientManager.Param param = new OkHttpClientManager.Param(key, value);
        return param;
    }
}
