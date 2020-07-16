package com.example.project8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class video extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final String TAG = "video";
    private SurfaceView mSurfaceView;
    private Button mRecordBtn, mPlayBtn;
    private boolean mIsRecord = false; //是否正在录像
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private String mMediaPath;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private PermissionsUtils mPermissionsUtils;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //6.0及以上系统请求运行时权限 利用权限申请工具类（见下文）
        requestCameraAndStoragePermission();
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 必须-设置Surface不需要维护自己的缓冲区
        mRecordBtn =  findViewById(R.id.record_btn);
        mPlayBtn = findViewById(R.id.play_btn);
        initBtnClick();
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);
    }
    private void requestCameraAndStoragePermission() {
        //检查用户是否授权
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(video.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //没有授权则请求相应权限
                ActivityCompat.requestPermissions(video.this, new String[]{permissions[i]}, 1);
            }
        }
        //利用权限申请工具类来实现
        mPermissionsUtils = PermissionsUtils.getInstance();
        mPermissionsUtils.chekPermissions(video.this,permissions, permissionsResult);
    }
    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
//   StartRecording(); 注意这里的逻辑 并不是权限通过了就立即开始录像了 而是权限通过了 就可以打开Camera进行预览
            mCamera = Camera.open(0);//Camera.CameraInfo.CAMERA_FACING_BACK
        }
        @Override
        public void forbitPermissons() {
            Toast.makeText(video.this, "You denyied the permission", Toast.LENGTH_SHORT).show();
        }
    };
    private void StartRecording(){
        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsRecord) {
                    try {
                        Log.d(TAG, "首次点击开始录像 ");
                        if (prepareVideoRecorder()) {
                            mMediaRecorder.start();
                            mIsRecord = true;
                            mRecordBtn.setText("stop");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "再次点击停止录像");
                    mMediaRecorder.stop();
                    releaseMediaRecorder();
                    mCamera.lock();
                    mRecordBtn.setText("record");
                    mIsRecord = false;
                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                }
            }
        });
    }
    private void initBtnClick() {
        StartRecording();
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.reset();
                    uri = Uri.parse(mMediaPath);
                    mMediaPlayer = MediaPlayer.create(video.this,uri);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setDisplay(mSurfaceHolder);
                    try{
                        mMediaPlayer.prepare();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mMediaPlayer.start();
                }
            }
        });
    }
    /*
     * 相机预览前的准备工作代码 单独抽出来
     * */
    private boolean prepareVideoRecorder() throws IOException {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.reset();
        }
        /*camera相关设置部分*/
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mCamera = Camera.open(0);//Camera.CameraInfo.CAMERA_FACING_BACK
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            //设置旋转角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了
            mCamera.lock();
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);

        }
        /*recorder设置部分*/
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(getOutputMediaFile());
        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mMediaRecorder.prepare();
        return true;
    }
    /*
     * 获取手机外部存储路径
     * */
    private String getOutputFile() {
        File mediaFile = null;
        boolean OutputExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (OutputExist) {
            mediaFile = Environment.getExternalStorageDirectory();
            return mediaFile.toString();
        }
        return null;
    }
    /*
     * 获取录制视频的日期 作为存储文件路径一部分
     * */
    private String getDate() {
        Log.d(TAG, "获取录制视频的日期 ");
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);   // 获取年份
        int month = ca.get(Calendar.MONTH);   // 获取月份
        int day = ca.get(Calendar.DATE);   // 获取日
        String date = "" + year + "_" + (month + 1) + "_" + day;
        return date;
    }
    /*
     *创建视频存储文件夹 录制好的视频存储在手机外部存储中 以录像时间+mp4格式命名
     * */
    private String getOutputMediaFile() {
        Log.d(TAG, "获取视频存储的位置 ");
        String mediaPath = getOutputFile();
        if (mediaPath != null) {
            File mediaFile = new File(mediaPath + "/recordVideo");
            if (!mediaFile.exists()) {
                mediaFile.mkdir();
            }
            return mMediaPath = mediaFile.getAbsolutePath() + File.separator + getDate() + ".mp4";
        }
        return null;
    }
    /*
     * 录制视频结束时释放相机资源
     * */
    private void releaseMediaRecorder() {
        Log.d(TAG, "录制结束后释放资源 ");
        if (mMediaRecorder != null) {
            mMediaRecorder.reset(); // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();   // lock camera for later use
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mSurfaceHolder = surfaceHolder;
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceView = null;
        mSurfaceHolder = null;
        releaseMediaRecorder();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    public static class PermissionsUtils {
        private final int mRequestCode = 100;//权限请求码
        public static boolean showSystemSetting = true;
        private PermissionsUtils() {
        }
        private static PermissionsUtils permissionsUtils;
        private IPermissionsResult mPermissionsResult;
        /*
         * 单例模式创建PermissionUtils实例 工具类中的静态方法可以直接使用类名+方法名调用 非静态方法还是需要获取到工具类的实例 实例对方法进行调用
         * */
        public static PermissionsUtils getInstance() {
            if (permissionsUtils == null) {
                synchronized (PermissionsUtils.class) {
                    if (permissionsUtils == null)
                        permissionsUtils = new PermissionsUtils();
                }
            }
            return permissionsUtils;
        }
        /*
         * 检查用户是否授权 + 如果没有授权 则申请授权 - 系统标准方法
         * */
        public void chekPermissions(Activity context, String[] permissions, @NonNull IPermissionsResult permissionsResult) {
            mPermissionsResult = permissionsResult;
            if (Build.VERSION.SDK_INT < 23) {//6.0系统及以上才会动态申请权限 以下不用 所以直接return出去
                permissionsResult.passPermissons();
                return;
            }
            //创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
            List<String> mPermissionList = new ArrayList<>();
            //逐个判断你要的权限是否已经通过
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);//添加还未授予的权限
                }
            }
            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(context, permissions, mRequestCode);
            } else {
                //说明权限都已经通过，利用接口变量调用实现的接口方法 即有权限之后需要调用的方法
                permissionsResult.passPermissons();
                return;
            }
        }
        //请求权限后回调的方法
        //参数： requestCode 是我们自己定义的权限请求码
        //参数： permissions 是我们请求的权限名称数组
        //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
        public void onRequestPermissionsResult(Activity context, int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            boolean hasPermissionDismiss = false;//有权限没有通过
            if (mRequestCode == requestCode) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        hasPermissionDismiss = true;
                    }
                }
                //如果有权限没有被允许
                if (hasPermissionDismiss) {
                    if (showSystemSetting) {
                        showSystemPermissionsSettingDialog(context);//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                    } else {
                        mPermissionsResult.forbitPermissons();
                    }
                } else {
                    //全部权限通过，可以进行下一步操作。。。
                    mPermissionsResult.passPermissons();
                }
            }
        }
        /**
         * 不再提示权限时的展示对话框
         */
        AlertDialog mPermissionDialog;
        private void showSystemPermissionsSettingDialog(final Activity context) {
            final String mPackName = context.getPackageName();
            if (mPermissionDialog == null) {
                mPermissionDialog = new AlertDialog.Builder(context)
                        .setMessage("已禁用权限，请手动授予")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelPermissionDialog();
                                Uri packageURI = Uri.parse("package:" + mPackName);
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                context.startActivity(intent);
                                context.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //关闭页面或者做其他操作
                                cancelPermissionDialog();
                                //mContext.finish();
                                mPermissionsResult.forbitPermissons();
                            }
                        })
                        .create();
            }
            mPermissionDialog.show();
        }
        //关闭对话框
        private void cancelPermissionDialog() {
            if (mPermissionDialog != null) {
                mPermissionDialog.cancel();
                mPermissionDialog = null;
            }
        }
        public interface IPermissionsResult {
            void passPermissons();
            void forbitPermissons();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null)
            initBtnClick();
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }
}
