package com.fly.run.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IOTools {
    private final static String TAG = "IOTools";

    //写数据到SD中的文件
    public static void writeFileSdcardFile(String fileName, String write_str) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读SD中的文件
    public static String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 读取一个文件到object
     *
     * @return
     */
    public static final Object readObject(String path) {
        try {
            if (path == null || path.length() == 0)
                return null;
            File file = new File(path);
            if (!file.exists())
                return null;
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
            Object result = in.readObject();
            in.close();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保存一个object到文件
     *
     * @param path   文件
     * @param objcet 对象
     */
    public static final boolean writeObject(String path, Object objcet) {
        try {
            if (path == null || path.length() == 0)
                return false;
            File file = new File(path);
            if (file.exists())//先删除旧的文件
                file.delete();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(objcet);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static final boolean writeBitmap2JPG(File file, Bitmap bmp) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 4.0之后在主线程里面执行Http请求都会报这个错android.os.NetworkOnMainThreadException
     *
     * @param urlString          网络地址
     * @param filename           保存路径
     * @param saveBitmapCallBack 回调接口
     */
    public static final void saveBitmapInChildThread(final String urlString, final String filename, final SaveBitmapCallBack saveBitmapCallBack) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (saveBitmapCallBack == null) {
                    return;
                }
                saveBitmapCallBack.notice((Boolean) msg.obj);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = saveBitmap(urlString, filename);
                Message message = Message.obtain();
                message.obj = isSuccess;
                handler.sendMessage(message);
            }
        }).start();
    }

    public static interface SaveBitmapCallBack {
        public void notice(boolean isSuccess);
    }

    public static final boolean saveBitmap(String urlString, String filename) {
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoInput(true);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存一个字符串到文件
     *
     * @param path
     * @param content
     * @return
     */
    public static boolean writeString(String path, String content) {
        ByteArrayInputStream in = null;
        FileOutputStream out = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                Logger.i("--->UN 构建csv邮件成功！");
            }
            in = new ByteArrayInputStream(content.toString().getBytes());
            out = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除指定路径下的全部文件，如果文件个数超过值
     */
    public static final boolean deletePathFilesifMore(String path, long count) {

        if (TextUtils.isEmpty(path))
            return false;

        try {
            File f = new File(path);

            File[] ff = f.listFiles();
            if (ff == null)
                return true;

            Logger.i(TAG, "deletePathFilesifMore path:" + path + ", count:" + ff.length);
            if (ff.length < count)
                return true;

            for (File curFile : ff) {
                if (curFile != null) {
                    Logger.i(TAG, "deletePathFilesifMore file:" + curFile.getAbsolutePath());
                    curFile.delete();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除指定路径下时间值小于指定限制的文件，
     *
     * @param path
     * @param timeLimit 毫秒
     * @return
     */
    public static final boolean deletePathFilesOldFile(String path, long timeLimit) {
        if (TextUtils.isEmpty(path))
            return false;

        try {
            File f = new File(path);

            File[] ff = f.listFiles();
            if (ff == null)
                return true;

            Logger.i(TAG, "deletePathFilesOldFile path:" + path + ", count:" + ff.length);

            for (File curFile : ff) {
                if (curFile != null && (curFile.lastModified() < timeLimit || curFile.lastModified() > System.currentTimeMillis())) {
                    Logger.i(TAG, "deletePathFilesOldFile file:" + curFile.getAbsolutePath());
                    curFile.delete();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
