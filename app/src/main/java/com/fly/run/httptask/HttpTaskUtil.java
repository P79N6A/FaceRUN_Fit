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

        public void onFailure(Request request, IOException e);
    }

    public HttpTaskUtil setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public void RegisterTask(String account, String password) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("Account", account);
            OkHttpClientManager.Param paramPassword = new OkHttpClientManager.Param("Password", password);
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

    public void LoginTask(String account, String password) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("Account", account);
            OkHttpClientManager.Param paramPassword = new OkHttpClientManager.Param("Password", password);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_LOGIN, new OkHttpClientManager.StringCallback() {
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

    public void QueryUserTask(String account) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("Account", account);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_USER_QUERY, new OkHttpClientManager.StringCallback() {
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

    public void SaveActiveTask(String account, String active_id, String title, String content,
                               String content_img, String active_time, String address,
                               String use_time, String distance, String level) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("account", account);
            OkHttpClientManager.Param paramActiveId = new OkHttpClientManager.Param("active_id", active_id);
            OkHttpClientManager.Param paramTitle = new OkHttpClientManager.Param("title", title);
            OkHttpClientManager.Param paramContent = new OkHttpClientManager.Param("content", content);
            OkHttpClientManager.Param paramContentImg = new OkHttpClientManager.Param("content_img", content_img);
            OkHttpClientManager.Param paramActiveTime = new OkHttpClientManager.Param("active_time", active_time);
            OkHttpClientManager.Param paramAddress = new OkHttpClientManager.Param("address", address);
            OkHttpClientManager.Param paramUseTime = new OkHttpClientManager.Param("use_time", use_time);
            OkHttpClientManager.Param paramDistance = new OkHttpClientManager.Param("distance", distance);
            OkHttpClientManager.Param paramLevel = new OkHttpClientManager.Param("level", level);

            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_ACTIVE_SAVE, new OkHttpClientManager.StringCallback() {
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
                    }, paramAccount, paramActiveId, paramTitle, paramContent, paramContentImg, paramActiveTime, paramAddress,
                    paramUseTime, paramDistance, paramLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QueryAllActiveListTask() {
        try {
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_ACTIVE_QUERY_ALL, new OkHttpClientManager.StringCallback() {
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


    public void QueryUserActivesTask(String account) {
        try {
            OkHttpClientManager.Param paramAccount = new OkHttpClientManager.Param("Account", account);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_ACTIVE_QUERY_USER, new OkHttpClientManager.StringCallback() {
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

    public void DeleteActiveTask(String id, String active_id) {
        try {
            OkHttpClientManager.Param paramID = new OkHttpClientManager.Param("ID", id);
            OkHttpClientManager.Param paramActiveId = new OkHttpClientManager.Param("ActiveId", active_id);
            OkHttpClientManager.getInstance()._postAsyn(UrlConstants.HTTP_ACTIVE_DELETE, new OkHttpClientManager.StringCallback() {
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
            }, paramID, paramActiveId);
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
