package com.fly.run.config;

import java.util.ArrayList;

public class AppConstants {
    public static String[] LocationMode = {"精确定位(GPS)", "模糊定位(WIFI)"};
    public static String[] SportMode = {"跑步", "骑行", "开车"};

    public static String Broadcast_Receiver_Time = "Broadcast.Receiver.Time";

    public static String _SPEED_NOW_STR = "当前时速";
    public static String RUN_SPEED_SLOW_STR = "速度较慢";
    public static int RUN_SPEED_SLOW = 6;

    public static String RUN_SPEED_MID_STR = "速度适中";
    public static int RUN_SPEED_MID = 9;

    public static String RUN_SPEED_FAST_STR = "速度较快";

    public static String RUN_SPEED_ERROR_STR = "GPS没有获取到数据";

    public static String mSampleDirPath;
    public static final String SAMPLE_DIR_NAME = "baiduTTS";
    public static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
//    public static final String SPEECH_MALE_MODEL_NAME = "bd_etts_ch_speech_male.dat";
//    public static final String TEXT_MODEL_NAME = "bd_etts_ch_text.dat";
    public static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    public static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    public static final String LICENSE_FILE_NAME = "temp_license_2017-01-17.txt";
    public static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    public static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    public static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    public static final int PRINT = 0;
    public static final int UI_CHANGE_INPUT_TEXT_SELECTION = 1;
    public static final int UI_CHANGE_SYNTHES_TEXT_SELECTION = 2;

    public static int AUDIO_VOLUME_CURRENT = 0;
    public static int AUDIO_VOLUME_MAX = 0;



}
