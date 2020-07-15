package com.example.project8;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class picture extends AppCompatActivity {

    private SurfaceView sv_main_surface;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        sv_main_surface = (SurfaceView) findViewById(R.id.sv_main_surface);

        sv_main_surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //打开照相机
                camera = Camera.open();
                //设置参数
                Camera.Parameters parameters=camera.getParameters();

                parameters.setPictureFormat(PixelFormat.JPEG);

                parameters.set("jpeg-quality",85);

                camera.setParameters(parameters);

                //将画面展示到SurfaceView
                try {
                    camera.setPreviewDisplay(sv_main_surface.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //开启预览效果
                camera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                if(camera!=null){
                    camera.stopPreview();
                    camera.release();
                    camera=null;
                }
            }
        });

    }

    public void takePhoto(View view){
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //技术：图片压缩技术

                Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                try {
                    FileOutputStream fos=new FileOutputStream("/mnt/sdcard/G150820_"+System.currentTimeMillis()+".png");
                    bitmap.compress(Bitmap.CompressFormat.PNG,85,fos);

                    camera.stopPreview();
                    camera.startPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}