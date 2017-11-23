package com.fly.run.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.fly.run.config.AppConstants;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.utils.MediaUtil;
import com.fly.run.utils.SDCardUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.fly.run.config.Constant.Config;
import static com.fly.run.config.Constant.UserConfigPath;


/**
 * Created by kongwei on 2017/2/16.
 */

public class App extends Application {

    private static final java.lang.String TAG = "App";
    public static App instance = null;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        getRunData();
        DisplayUtil.displayScreen(this);
        initImageLoader(this);
        UserConfigPath = SDCardUtil.buildConfigDir(this, Config);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context).threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .diskCacheSize(50 * 1024 * 1024)
//                // 50 Mb
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
//                .build();

        int maxMemory = 0;
        int maxImageMemoryCacheSize = (maxMemory == 0) ? ImageLoaderOptions.MAX_IMAGE_DISK_CACHE_SIZE : (maxMemory / 8);
//		File cacheDir = StorageUtils.getOwnCacheDirectory(appContext, "Melinked/imageloader/Cache");
// 				.diskCache(new UnlimitedDiskCache(cacheDir)) //自定义缓存路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCache(new LruMemoryCache(maxImageMemoryCacheSize))
                .memoryCacheExtraOptions(ImageLoaderOptions.MAX_IMAGE_WIDTH, ImageLoaderOptions.MAX_IMAGE_HEIGHT)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
//				.diskCacheSize(ImageLoaderOptions.MAX_IMAGE_DISK_CACHE_SIZE)//缓存的文件占sdcard大小
//				.diskCacheFileCount(ImageLoaderOptions.MAX_IMAGE_DISK_FILE_COUNT)//缓存的文件数量
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO) //LIFO:后进先出 --  FIFO:先入先出
                .build();

        ImageLoader.getInstance().init(config);
    }

    private void getRunData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (MediaUtil.mediaEntityList == null)
                    MediaUtil.mediaEntityList = MediaUtil.getAllMediaList(getInstance(), null);
                initialEnv();
            }
        }).start();
    }

    /**
     * 语音唤醒文件准备
     */
    private void initialEnv() {
        if (AppConstants.mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            AppConstants.mSampleDirPath = sdcardPath + "/" + SDCardUtil.localAppDir + "/" + AppConstants.SAMPLE_DIR_NAME;
        }
        makeDir(AppConstants.mSampleDirPath);
        copyFromAssetsToSdcard(false, AppConstants.SPEECH_FEMALE_MODEL_NAME, AppConstants.mSampleDirPath + "/" + AppConstants.SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, AppConstants.SPEECH_MALE_MODEL_NAME, AppConstants.mSampleDirPath + "/" + AppConstants.SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, AppConstants.TEXT_MODEL_NAME, AppConstants.mSampleDirPath + "/" + AppConstants.TEXT_MODEL_NAME);
//        copyFromAssetsToSdcard(false, AppConstants.LICENSE_FILE_NAME, AppConstants.mSampleDirPath + "/" + AppConstants.LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + AppConstants.ENGLISH_SPEECH_FEMALE_MODEL_NAME, AppConstants.mSampleDirPath + "/"
                + AppConstants.ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + AppConstants.ENGLISH_SPEECH_MALE_MODEL_NAME, AppConstants.mSampleDirPath + "/"
                + AppConstants.ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + AppConstants.ENGLISH_TEXT_MODEL_NAME, AppConstants.mSampleDirPath + "/"
                + AppConstants.ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
