package com.recuerdapp.recuerdapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.recuerdapp.database.Contacto;
import com.recuerdapp.database.DatabaseHandler;
import com.recuerdapp.database.Perfil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by 79192 on 16/11/2017.
 */

public class CapturarFotomemoriaActivity extends ActionBarActivity implements Camera.PreviewCallback, SurfaceHolder.Callback, SensorEventListener {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder sHolder;
    private ImageView button;

    ImageView switchCamButton;

    private Camera.Size cameraSize;

    private SensorManager sensorManager;
    private Sensor sensor=null;

    private  boolean selectedCamera;

    private int orientation=0;

    private int cameraDirection;


    boolean set=false;

    private byte[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        set=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_videomemoria);
        button=(ImageView)findViewById(R.id.captureButton);
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
                camera.setPreviewCallback(this);
            } catch (Exception e) {
                setupCamera(i);
            }
        }
        else{
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(holder.getSurface()!=null){
            if(camera!=null) {
                camera.stopPreview();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.setPreviewCallback(this);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        this.data=data;
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
    public byte[] rotateYuv90Deg(int height,int width,byte[]data){
        byte [] processed = new byte[width*height*3/2];
        int i = 0;
        for(int x = 0;x < width;x++)
        {
            for(int y = height-1;y >= 0;y--)
            {
                processed[i] = data[y*width+x];
                i++;
            }
        }
        i = width*height*3/2-1;
        for(int x = width-1;x > 0;x=x-2)
        {
            for(int y = 0;y < height/2;y++)
            {
                processed[i] = data[(width*height)+(y*width)+x];
                i--;
                processed[i] = data[(width*height)+(y*width)+(x-1)];
                i--;
            }
        }
        return processed;
    }
    public void onButtonPressed(View view) {
        try{
            OutputStream outStream = null;
            String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM";
            Date date=new Date();
            File dir=new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String extra="";
            dir=new File(path + "/recuerdapp_"+String.valueOf(date.getMonth())
                    +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+".jpg");
            while(dir.exists()){
                dir=new File(path + "/recuerdapp_"+String.valueOf(date.getMonth())
                        +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+extra+".jpg");

                extra+="0";
            }
            String filePath=path;
            String fileName="recuerdapp_"+String.valueOf(date.getMonth())
                    +"_"+String.valueOf(date.getDate())+"_"+String.valueOf(date.getMinutes())+"_"+String.valueOf(date.getSeconds())+extra+".jpg";
            File file=new File(path+"/"+fileName);

            if(file.exists()){
                file.delete();
            }
            file.createNewFile();



            outStream=new FileOutputStream(file);


            YuvImage yuvImage;


            if(orientation==0){
                byte[] processed;
                if(cameraDirection==Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    processed = rotateYuv90Deg(cameraSize.height, cameraSize.width, data);
                    processed=rotateYuv90Deg(cameraSize.width,cameraSize.height,processed);
                    processed=rotateYuv90Deg(cameraSize.height,cameraSize.width,processed);
                }
                else{
                    processed = rotateYuv90Deg(cameraSize.height, cameraSize.width, data);
                }
                yuvImage = new YuvImage(processed,ImageFormat.NV21,cameraSize.height,cameraSize.width,null);
                yuvImage.compressToJpeg(new Rect(0, 0,cameraSize.height,cameraSize.width),25 , outStream);            }
            else if(orientation==1){
                yuvImage = new YuvImage(data,ImageFormat.NV21,cameraSize.width,cameraSize.height,null);
                yuvImage.compressToJpeg(new Rect(0, 0, cameraSize.width,cameraSize.height),25 , outStream);
            }
            else if(orientation==2){
                byte[] processed;
                if(cameraDirection==Camera.CameraInfo.CAMERA_FACING_FRONT){
                    processed = rotateYuv90Deg(cameraSize.height, cameraSize.width, data);
                }
                else{
                    processed = rotateYuv90Deg(cameraSize.height, cameraSize.width, data);
                    processed = rotateYuv90Deg(cameraSize.width, cameraSize.height, processed);
                    processed = rotateYuv90Deg(cameraSize.height, cameraSize.width, processed);
                }
                yuvImage = new YuvImage(processed,ImageFormat.NV21,cameraSize.height,cameraSize.width,null);
                yuvImage.compressToJpeg(new Rect(0, 0, cameraSize.height,cameraSize.width), 25, outStream);
            }
            else if(orientation==3){
                byte[] processed=rotateYuv90Deg(cameraSize.height,cameraSize.width,data);
                processed=rotateYuv90Deg(cameraSize.width,cameraSize.height,processed);
                yuvImage = new YuvImage(processed,ImageFormat.NV21,cameraSize.width,cameraSize.height,null);
                yuvImage.compressToJpeg(new Rect(0, 0,cameraSize.width,cameraSize.height),25 , outStream);
            }
            Intent intent=getIntent();
            boolean k=intent.getBooleanExtra("profile",false);
            boolean p=intent.getBooleanExtra("contact",false);
            int id=intent.getIntExtra("contact_id",-1);
            if(k){
                Bitmap b= BitmapFactory.decodeFile(path+"/"+fileName);
                Perfil.setProfile(b);
            }
            else if(p){
                DatabaseHandler handler=new DatabaseHandler(this);
                Contacto contacto=handler.getContacto(id);
                Bitmap b=BitmapFactory.decodeFile(path+"/"+fileName);
                contacto.setContactoProfilePicture(b);
            }
            else {
                Intent data = new Intent();
                data.putExtra("file_name", fileName);
                setResult(RESULT_OK,data);
            }
            finish();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(set){
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
                if (orientation != 0 && Math.abs(sensorEvent.values[0]) <= 2.0f && sensorEvent.values[1] >= 5.0f) {
                    orientation = 0;
                    switchCamButton.setRotation(0.0f);
                }
                if (orientation != 1 && Math.abs(sensorEvent.values[1]) <= 2.0f && sensorEvent.values[0] >= 5.0f) {
                    orientation = 1;
                    switchCamButton.setRotation(90.0f);
                }

                if (orientation != 2 && Math.abs(sensorEvent.values[0]) <= 2.0f && sensorEvent.values[1] <= -5.0f) {
                    orientation = 2;
                    switchCamButton.setRotation(180.0f);
                }
                if (orientation != 3 && Math.abs(sensorEvent.values[1]) <= 2.0f && sensorEvent.values[0] <= -5.0f) {
                    orientation = 3;
                    switchCamButton.setRotation(270.0f);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}
