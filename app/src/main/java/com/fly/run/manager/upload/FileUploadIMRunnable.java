package com.fly.run.manager.upload;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.fly.run.bean.UpTokenInfo;
import com.fly.run.config.UrlConstants;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.upload.callback.UploadCallback;
import com.fly.run.utils.TimeFormatUtils;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileUploadIMRunnable implements Runnable {

    private final String TAG = getClass().getSimpleName();
    private boolean isUploading = false;
    private String ak = "kbOWObiU4NTMU4fVaNPeQkRQ5nnVbBAt0AMSTH1e";
    private String sk = "MkzntYTU4h2DQHiOtXnlq5URAEDZPs9T85lKaSts";
    private volatile boolean isCancelled = false;

    private Configuration config;
    private UploadManager uploadManager;

    private Map<String, String> mFilePaths;
    private String filePath = "";
    private UploadCallback mUploadCallback;

    private HttpTaskUtil httpTaskUtil;

    public FileUploadIMRunnable(Map<String, String> filePaths, UploadCallback callback) {
        this.mFilePaths = filePaths;
        this.filePath = (mFilePaths != null && mFilePaths.containsKey("file")) ? mFilePaths.get("file") : "";
        this.mUploadCallback = callback;
        initUploadManager();
    }

    public FileUploadIMRunnable(String filePath, UploadCallback callback) {
        this.filePath = filePath;
        this.mUploadCallback = callback;
        initUploadManager();
    }

    private void initUploadManager() {
        if (config == null)
            config = new com.qiniu.android.storage.Configuration.Builder()
                    .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
                    .useHttps(false)               // 是否使用https上传域名
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
                    .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
                    //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                    //.zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .zone(AutoZone.autoZone)
                    .build();
        if (uploadManager == null)
            uploadManager = new UploadManager(config);
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
        }
    }

    public boolean isUploading() {
        return isUploading;
    }

    @Override
    public void run() {
        doExecutor();
    }

    public void doExecutor() {
        if (isUploading)
            return;
        final File file = new File(filePath);
        if (file != null || file.exists() || file.length() > 0) {
            uploadFile(file);
        }
    }

    private void uploadFile(final File file) {
        try {
            isUploading = true;
            if (file == null || !file.exists()) {
                if (mUploadCallback != null)
                    mUploadCallback.error();
                return;
            }
            final String fileName = file.getName();
            String key = fileName;//<指定七牛服务上的文件名，或 null>;
            String token = "";
            String response = httpTaskUtil.GetUploadTokenInfoTask(buildUpTokenParam(key));
            UpTokenInfo upTokenInfo = JSONObject.parseObject(response,UpTokenInfo.class);
            if (upTokenInfo != null) {
                token = upTokenInfo.getToken();
                key = upTokenInfo.getRandomFileName();
                String dateFormat = TimeFormatUtils.getChatFileFormatDate();
                if (TextUtils.isEmpty(dateFormat))
                    dateFormat = "";
                key = dateFormat + "/" + key;
            }
            uploadManager.put(file, key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo responseInfo, org.json.JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            try {
                                if (responseInfo.isOK()) {
                                    Log.e(TAG, "Upload Success key = " + key);
                                    if (mUploadCallback != null) {
                                        JSONObject result = new JSONObject();
                                        result.put("code", 200);
                                        result.put("fileName", fileName);
                                        result.put("fileSize", file.length());
                                        result.put("url", UrlConstants.HTTP_QINIU_DATA_IM_FILE + key);
                                        if (mUploadCallback != null)
                                            mUploadCallback.success(result.toString());
                                    }
                                } else {
                                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                    Log.e(TAG, "Upload Fail");
                                    if (mUploadCallback != null)
                                        mUploadCallback.error();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                if (mUploadCallback != null)
                                    mUploadCallback.error();
                            }

                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                            if (mUploadCallback != null)
                                mUploadCallback.progress((int) (percent * 100));
                            Log.e(TAG, "key = " + key + "  percent = " + percent);
                            if (percent >= 1) {

                            }
                        }
                    }, new UpCancellationSignal() {
                        public boolean isCancelled() {
                            return isCancelled;
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception = " + ((e != null) ? e.getMessage() : ""));
            if (mUploadCallback != null)
                mUploadCallback.error();
        } finally {
            isUploading = false;
            Log.e(TAG, "finally = mUploadCallback.end()");
        }
    }

    public Map<String, String> buildUpTokenParam(String fileName) {
        Map<String, String> param = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileName", fileName);
            param.put("policyJsonStr", jsonObject.toJSONString());
            param.put("bucket", "imfile");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }
}
