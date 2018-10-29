package com.recuerdapp.recuerdapp;

/**
 * Created by 79192 on 16/11/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Exchanger;

public class CapturarVideomemoriaActivity extends ActionBarActivity implements SurfaceHolder.Callback, SensorEventListener {
    private TextView recordingMessage;

    private String videoName;

    private boolean recording;

    private boolean videoIsStoppable;

    private SurfaceView surfaceView;
    private SurfaceHolder sHolder;
    private SurfaceHolder.Callback sHCallback;
    private MediaRecorder mediaRecorder;
    Camera.Size cameraSize;
    private Camera camera;
    private ImageView button;

    ImageView switchCamButton;

    private SensorManager sensorManager;
    private Sensor sensor=null;

    private  boolean selectedCamera;

    private int orientation=0;

    private int cameraDirection;


    boolean set=false;

    private byte[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_videomemoria);
        set=false;
        recording=false;
        button=(ImageView)findViewById(R.id.captureButton);
        button.setImageResource(R.mipmap.ic_record);
        orientation=0;
        switchCamButton=(ImageView)findViewById(R.id.cameraSwitchButton);
        selectedCamera=false;
        setupCamera(0);
        if(Camera.getNumberOfCameras()>1) {
            switchCamButton.setVisibility(View.VISIBLE);
            switchCamButton.setImageResource(R.mipmap.ic_switch_camera);
            switchCamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedCamera=!selectedCamera;
                    if(selectedCamera){
                        setupCamera(1);
                    }
                    else{
                        setupCamera(0);
                    }
                }
            });
        }
        else{
            switchCamButton.setVisibility(View.INVISIBLE);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        else if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }

        new CountDownTimer(10L,1L) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                set=true;
            }
        }.start();
    }

    private void setupCamera(int i){
        if(i<Camera.getNumberOfCameras()){
            if(camera!=null){
                camera.lock();
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera=null;
            }
            try {

                camera = Camera.open(i);
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                cameraDirection=cameraInfo.facing;


                camera.setDisplayOrientation(90);
                Camera.Parameters parameters=camera.getParameters();
                List<Camera.Size> sizes=parameters.getSupportedPreviewSizes();
                cameraSize=sizes.get(0);
                parameters.setPreviewSize(cameraSize.width,cameraSize.height);
                if(parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                parameters.setPreviewFormat(ImageFormat.NV21);
                camera.setParameters(parameters);
                camera.setPreviewDisplay(sHolder);
                camera.startPreview();
            } catch (Exception e) {
                setupCamera(i);
            }
        }
        else{
            Intent data=new Intent();
            setResult(RESULT_CANCELED,data);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        surfaceView=(SurfaceView)findViewById(R.id.surfaceViewVideoCp);
        sHolder=surfaceView.getHolder();
        sHolder.addCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(sensor!=null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensor!=null)
            sensorManager.unregisterListener(this);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }
    private String prepareRecorder(){
        mediaRecorder = new MediaRecorder();

        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));



        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM";
        Date date=new Date();
        File dir=new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String extra="";
        dir=new File(path + "/recuerdapp_"+String.valueOf(date.getMonth())
                +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+".mp4");
        while(dir.exists()){
            dir=new File(path + "/recuerdapp_"+String.valueOf(date.getMonth())
                    +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+extra+".mp4");

            extra+="0";
        }
        mediaRecorder.setOutputFile(path + "/recuerdapp_"+String.valueOf(date.getMonth())
                +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+extra+".mp4");
        mediaRecorder.setOrientationHint(90*orientation);

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName="recuerdapp_"+String.valueOf(date.getMonth())
                +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+extra+".mp4";
        return fileName;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(holder.getSurface()!=null){
            if(camera!=null) {
                camera.stopPreview();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        sHolder.removeCallback(this);
        if(camera!=null) {

            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera=null;
        }
    }
    private void stopRecording(){
        try {
            set = false;
            button.setImageResource(R.mipmap.ic_record);
            switchCamButton.setVisibility(View.VISIBLE);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
            orientation = 0;
            switchCamButton.setRotation(0.0f);
            new CountDownTimer(10L, 1L) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    set = true;
                }
            }.start();
        }
        catch(Exception e){
        }
    }
    public void onButtonPressed(View view) {
        if(recording){
            if(videoIsStoppable) {
                stopRecording();
                Intent data=new Intent();
                data.putExtra("file_name",videoName);
                setResult(RESULT_OK,data);
                finish();
            }
            else
                return;
        }
        else{
            switchCamButton.setVisibility(View.INVISIBLE);
            button.setImageResource(R.mipmap.ic_stop_recording);
            videoName=prepareRecorder();
            mediaRecorder.start();
            videoIsStoppable=false;
            new CountDownTimer(1100L, 100L) {
                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {
                    videoIsStoppable = true;
                }
            }.start();
        }
        recording=!recording;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(set&&recording==false){
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                if (orientation != 1 && Math.abs(sensorEvent.values[0]) <= 2.0f && sensorEvent.values[1] >= 5.0f) {
                    orientation = 1;
                    switchCamButton.setRotation(0.0f);
                }
                if (orientation != 0 && Math.abs(sensorEvent.values[1]) <= 2.0f && sensorEvent.values[0] >= 5.0f) {
                    orientation = 0;
                    switchCamButton.setRotation(90.0f);
                }

                if (orientation != 3 && Math.abs(sensorEvent.values[0]) <= 2.0f && sensorEvent.values[1] <= -5.0f) {
                    orientation = 3;
                    switchCamButton.setRotation(180.0f);
                }
                if (orientation != 2 && Math.abs(sensorEvent.values[1]) <= 2.0f && sensorEvent.values[0] <= -5.0f) {
                    orientation = 2;
                    switchCamButton.setRotation(270.0f);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}
