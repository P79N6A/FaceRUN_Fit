package com.fly.run.activity.media;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.utils.AnimUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DisplayVideoActivity extends BaseUIActivity implements View.OnClickListener {

    private VideoView videoView;
    private ImageView iv_video_cover;
    private ImageView iv_video_play;

    private boolean isPrepared = false;
    private boolean mLoop = true;
    private boolean isError = false;


    private String strVideoPath = "http://video.melinked.com/c2908219-0c69-4fb7-b59e-d7ba9ddd5fae.mp4";
    private String strVideoCover = strVideoPath + "?vframe/jpg/offset/0";

    public static void startVideoActivity(Context context) {
        Intent intent = new Intent(context, DisplayVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);
        initView();
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoview);
        iv_video_cover = (ImageView) findViewById(R.id.iv_video_cover);
        iv_video_play = (ImageView) findViewById(R.id.iv_video_play);
        iv_video_cover.setOnClickListener(this);
        iv_video_play.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(strVideoCover, iv_video_cover);
        initVideo();
    }

    private void initVideo() {
//        videoView.setMediaController(new MediaController(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                    Log.e("DisplayVideoActivity", "what = " + what + "  extra = " + extra + "  isPlaying = " + mediaPlayer.isPlaying());
                    if (mediaPlayer.isPlaying()) {
                        AnimUtil.alphaAnimInVisible(iv_video_cover, 0, 1000);
                        AnimUtil.alphaAnimInVisible(iv_video_play, 0, 1000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                iv_video_cover.setVisibility(View.GONE);
                                iv_video_play.setVisibility(View.GONE);
                            }
                        }, 1050);
                        return true;
                    }
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                        showPlayerLoading.setVisibility(View.VISIBLE);
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
                        if (iv_video_cover.getAlpha() > 0) {
                            AnimUtil.alphaAnimInVisible(iv_video_cover, 0, 1000);
                            AnimUtil.alphaAnimInVisible(iv_video_play, 0, 1000);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    iv_video_cover.setVisibility(View.GONE);
                                    iv_video_play.setVisibility(View.GONE);
                                }
                            }, 1050);
                        }
                        if (mediaPlayer.isPlaying()) {
//                            showPlayerLoading.setVisibility(View.GONE);
                        }
                    }
                    return true;
                }
            });
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                isPrepared = true;
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.e("AnimalActivity", "视频播放完成");
                if (isError)
                    return;
                iv_video_cover.setVisibility(View.VISIBLE);
                iv_video_play.setVisibility(View.VISIBLE);
                AnimUtil.alphaAnimVisible(iv_video_play, 0, 400);
//                if (mLoop && !isError){
//                    mediaPlayer.start();
//                    mediaPlayer.setLooping(true);
//                }
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                isError = true;
                Log.e("AnimalActivity", "视频播放失败");
                switch (what) {
                    case -1004:
                        Log.e("Streaming Media", "MEDIA_ERROR_IO");
                        break;
                    case -1007:
                        Log.e("Streaming Media", "MEDIA_ERROR_MALFORMED");
                        break;
                    case 200:
                        Log.e("Streaming Media", "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                        break;
                    case 100:
                        Log.e("Streaming Media", "MEDIA_ERROR_SERVER_DIED");
                        break;
                    case -110:
                        Log.e("Streaming Media", "MEDIA_ERROR_TIMED_OUT");
                        break;
                    case 1:
                        Log.e("Streaming Media", "MEDIA_ERROR_UNKNOWN");
                        break;
                    case -1010:
                        Log.e("Streaming Media", "MEDIA_ERROR_UNSUPPORTED");
                        break;
                }
                switch (extra) {
                    case 800:
                        Log.e("Streaming Media", "MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case 702:
                        Log.e("Streaming Media", "MEDIA_INFO_BUFFERING_END");
                        break;
                    case 701:
                        Log.e("Streaming Media", "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 802:
                        Log.e("Streaming Media", "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 801:
                        Log.e("Streaming Media", "MEDIA_INFO_NOT_SEEKABLE");
                        break;
                    case 1:
                        Log.e("Streaming Media", "MEDIA_INFO_UNKNOWN");
                        break;
                    case 3:
                        Log.e("Streaming Media", "MEDIA_INFO_VIDEO_RENDERING_START");
                        break;
                    case 700:
                        Log.e("Streaming Media", "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_play:
                if (isPrepared) {
                    videoView.start();
                    iv_video_cover.setVisibility(View.GONE);
                    iv_video_play.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(strVideoPath)) {
                    Uri uri = Uri.parse(strVideoPath);
                    videoView.setVideoURI(uri);
                }
                break;
        }
    }
}
