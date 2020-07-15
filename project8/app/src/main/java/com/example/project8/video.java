package com.example.project8;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class video extends AppCompatActivity {

    private SurfaceView sv_recorder_surface;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        sv_recorder_surface = findViewById(R.id.sv_recorder_surface);

        //实例化媒体录制器
        mediaRecorder = new MediaRecorder();
    }

    public void start(View view){
        mediaRecorder.reset();

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        //设置格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        //设置保存路径
        mediaRecorder.setOutputFile("/mnt/sdcard/G150820_"+System.currentTimeMillis()+".mp4");

        mediaRecorder.setPreviewDisplay(sv_recorder_surface.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void stop(View view){
        if(mediaRecorder!=null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder=null;
        }
    }
}