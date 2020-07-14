package com.domker.study.androidstudy;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 使用MediaPlayer和SurfaceView播放resource视频
 */
public class MediaPlayerActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private TextView timeProcess;
    private Button convert_Button;
    int currentTime,totalTime;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MediaPlayer");

        setContentView(R.layout.layout_media_player);
        surfaceView = findViewById(R.id.surfaceView);
        seekBar=findViewById(R.id.process_Bar);
        convert_Button=findViewById(R.id.convert_Oriention);
        timeProcess=findViewById(R.id.process_Time);
        player = new MediaPlayer();
        try {
            player.setDataSource(getResources().openRawResourceFd(R.raw.bytedance));

            holder = surfaceView.getHolder();
            holder.addCallback(new PlayerCallBack());
            player.prepare();
            //时间处理
            totalTime=player.getDuration();
            currentTime=player.getCurrentPosition();
            String timeText=msecToTime(currentTime)+"/"+msecToTime(totalTime);
            timeProcess.setText(timeText);
            seekBar.setMax(totalTime);
            StartProgress();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 自动播放
//                    player.start();
                    player.setLooping(true);
                }
            });

            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    changeVideoSize(mp);
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println(percent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });

        findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });

        convert_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //变成竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //变成横屏了
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    setVideoParams(player,true);

                }
            }
        });
    }

    public void changeVideoSize(MediaPlayer mediaPlayer) {
        int surfaceWidth = surfaceView.getWidth();
        int surfaceHeight = surfaceView.getHeight();

        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        surfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    public void setVideoParams(MediaPlayer mediaPlayer, boolean isLand) {
        //取消全屏
        if(isLand==false)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            //设置全屏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void StartProgress(){
        //开辟新的Thread用于定期刷新SeekBar;
        DelayThread dThread = new DelayThread(100);
        dThread.start();
    }
    //开启一个线程进行实时刷新
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            seekBar.setProgress(player.getCurrentPosition());
            //时间处理
            totalTime=player.getDuration();
            currentTime=player.getCurrentPosition();
            String timeText=msecToTime(currentTime)+"/"+msecToTime(totalTime);
            timeProcess.setText(timeText);
        }
    };
    public class DelayThread extends Thread{
        int milliseconds;
        public DelayThread(int i){
            milliseconds=i;
        }
        public void run(){
            while(true){
                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    }
    static String msecToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int millisecond = 0;
        if (time <= 0)
            return "00:00:00.000";
        else {
            second = time / 1000;
            minute = second / 60;
            millisecond = time % 1000;
            if (second < 60) {
                timeStr = "00:00:" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else if (minute < 60) {
                second = second % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second) + "." + unitFormat2(millisecond);
            } else {// 数字>=3600 000的时候
                hour = minute / 60;
                minute = minute % 60;
                second = second - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second) + "."
                        + unitFormat2(millisecond);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {// 时分秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    public static String unitFormat2(int i) {// 毫秒的格式转换
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "00" + Integer.toString(i);
        else if (i >= 10 && i < 100) {
            retStr = "0" + Integer.toString(i);
        } else
            retStr = "" + i;
        return retStr;
    }
}
