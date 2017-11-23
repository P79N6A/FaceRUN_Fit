package com.fly.run.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.fly.run.app.App;

import java.io.IOException;
import java.util.Random;

/**
 * Created by xinzhendi-031 on 2016/11/17.
 */
public class MediaPlayerUtil implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

    private Context context;
    private static MediaPlayer mediaPlayer;
    private String prePath, nextPath;
    private String mediaPath;

    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = 0;
    private int selectIndex = -1;

    public static MediaPlayerUtil controll;

    public String getMediaPath() {
        return mediaPath;
    }

    public MediaPlayerUtil setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }

    public static MediaPlayerUtil getInstance() {
        if (controll == null)
            controll = new MediaPlayerUtil();
        return controll;
    }

    public MediaPlayerUtil() {
    }

    public MediaPlayerUtil(Context context) {
        this.context = context;
    }

    public MediaPlayer initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            AudioManagerUtil.setAudioVolume(App.getInstance());
        }
        return mediaPlayer;
    }

    public void setMediaSource(String path) {
        try {
            initMediaPlayer();
            resetMedia();
            mediaPath = path;
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mPlay = TYPE_PLAY;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playRandomMusic() {
        boolean open = SharePreferceTool.getInstance().getBoolean("SwitchMusic", true);
        if (!open)
            return;
        initMediaPlayer();
        if (MediaUtil.mediaEntityList != null && MediaUtil.mediaEntityList.size() > 0) {
            if (selectIndex == -1) {
                selectIndex = new Random().nextInt(MediaUtil.mediaEntityList.size());
                playNextOne();
            } else {
                playMedia(getMediaPath());
            }
        }
    }

    public int playMedia(String path) {
        boolean open = SharePreferceTool.getInstance().getBoolean("SwitchMusic", true);
        if (!open)
            return 0;
        initMediaPlayer();
        if (mediaPlayer != null) {
            if (TextUtils.isEmpty(mediaPath) || mPlay == 0) {
                setMediaSource(path);
                return 1;
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                mPlay = TYPE_PLAY;
                int loop = 0;
                int mVolume = (int) (getVolumePercent() * 100);
                while (loop < mVolume) {
                    loop++;
                    float volume = loop / 100.0f;
                    mediaPlayer.setVolume(volume, volume);
                    try {
                        Thread.sleep(24);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return 2;
            }
        }
        return 0;
    }

    public void pauseMedia() {
        initMediaPlayer();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int loop = (int) (getVolumePercent() * 100);
            while (loop > 0) {
                loop--;
                float volume = loop / 100.0f;
                mediaPlayer.setVolume(volume, volume);
                try {
                    Thread.sleep(24);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mediaPlayer.pause();
            mPlay = TYPE_PAUSE;
        }
    }

    private float getVolumePercent() {
        int currentVolume = AudioManagerUtil.getCurrentAudioVolume(App.getInstance());
        int maxVolume = AudioManagerUtil.getAudioMaxVolume(App.getInstance());
        float mVolumePercent = currentVolume / (float) maxVolume;
        return mVolumePercent <= 0 ? 0.5f : mVolumePercent;
    }

    public void resetMedia() {
        if (mediaPlayer != null)
            mediaPlayer.reset();
        mediaPath = "";
        mPlay = 0;
    }

    public void releaseMediaPlayer() {
        mPlay = 0;
        selectIndex = -1;
        setMediaPath("");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Logger.e("onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Logger.e("play over");
        playNextOne();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Logger.e("play error");
        resetMedia();
        releaseMediaPlayer();
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
    }

    public void playNextOne() {
        boolean open = SharePreferceTool.getInstance().getBoolean("SwitchMusic", true);
        if (!open)
            return;
        if (MediaUtil.mediaEntityList == null || MediaUtil.mediaEntityList.size() == 0)
            return;
        if (selectIndex == -1)
            selectIndex = new Random().nextInt(MediaUtil.mediaEntityList.size());
        else if (selectIndex >= (MediaUtil.mediaEntityList.size() - 1))
            selectIndex = 0;
        else
            selectIndex++;
        try {
            setMediaSource(MediaUtil.mediaEntityList.get(selectIndex).path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
