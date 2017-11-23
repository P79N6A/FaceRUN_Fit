package com.fly.run.utils;

import android.content.Context;
import android.media.AudioManager;

public class AudioManagerUtil {

    public static boolean checkAudioConnect(Context context) {
        AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        boolean isConnect = localAudioManager.isWiredHeadsetOn();
        return isConnect;
    }

    public static int getCurrentAudioVolume(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volune = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return volune;
    }

    public static int getAudioMaxVolume(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return maxVolume;
    }

    public static void setAudioVolume(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = getAudioMaxVolume(context);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume / 2), 0);//tempVolume:音量绝对值
    }

    public static void setAudioVolume(Context context, int tempVolume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tempVolume, 0);//tempVolume:音量绝对值
    }

    public static void setAudioVolume(Context context, int flag, int currentVolume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volune = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (flag == 0) {
            //降低音量，调出系统音量控制
            for (int i = currentVolume; i >= 0; i--)
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                        AudioManager.FX_FOCUS_NAVIGATION_UP);
        } else if (flag == 1) {
            //增加音量，调出系统音量控制
            for (int i = 0; i <= currentVolume; i++)
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                        AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }

    public static void setAudioVolumeTemp(Context context, int flag) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int temp = max / 8;

        if (flag == 0) {
            //降低音量，调出系统音量控制
            int volumeTo = volume - temp;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeTo < 0 ? 0 : volumeTo, 0);
        } else if (flag == 1) {
            //增加音量，调出系统音量控制
            int volumeTo = volume + temp;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeTo > max ? max : volumeTo, 0);
        }
    }
}
