package com.fly.run.httptask;

import android.text.TextUtils;

import com.fly.run.app.App;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.NearByBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.PhoneUtil;
import com.fly.run.utils.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;

/**
 * Created by kongwei on 2017/3/10.
 */

public class HttpTaskUtil {

    private ResultListener resultListener;

    private HttpTaskUtil mInstance;

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
            }, paramPageNum, paramPageSize);
        } catch (Exception e) {
            e.printStackTrace();
            if (resultListener != null)
                resultListener.onFailure(null, e);
        }
    }

    /**
     * 用户注册
     * */
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
     * */
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
            strUserCover = accountBean.getHead_portrait() == null ? "" : accountBean.getHead_portrait();
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
