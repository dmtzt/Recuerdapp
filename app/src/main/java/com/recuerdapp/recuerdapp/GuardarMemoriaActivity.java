package com.recuerdapp.recuerdapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recuerdapp.database.DatabaseHandler;
import com.recuerdapp.database.Memoria;
import com.recuerdapp.database.MemoriaImgYVid;
import com.recuerdapp.real_path_util.RealPathUtil;

import java.util.ArrayList;

/**
 * Created by 79192 on 21/11/2017.
 */

public class GuardarMemoriaActivity extends AppCompatActivity{
    private EditText descripcionET;

    private TextView submitButton;

    ImageView expandImages;

    private ImageView takePictureButton;
    private ImageView recordVideoButton;
    private ImageView attachFileButton;

    private ArrayList<MemoriaImgYVid> memoriaImgsYVids=new ArrayList<MemoriaImgYVid>();

    private ImageView[] imagePreviews;
    private ImageView[] quickCancel;

    private AppCompatActivity activity;



    private void setupPreviews(){
        for(int i=0;i<3;i++){
            imagePreviews[i].setVisibility(View.INVISIBLE);
            quickCancel[i].setVisibility(View.INVISIBLE);
        }
        for(int i=0;i<memoriaImgsYVids.size()&&i<3;i++){

            quickCancel[i].setVisibility(View.VISIBLE);
            MemoriaImgYVid imgYVid=memoriaImgsYVids.get(i);
            Bitmap bitmap;
            if(imgYVid.getTipo()==MemoriaImgYVid.TYPE_IMG_DIR) {
                String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                bitmap = BitmapFactory.decodeFile(pathMain+"/"+imgYVid.getDirURI());

            }
            else if(imgYVid.getTipo()==MemoriaImgYVid.TYPE_VID_DIR){
                String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                bitmap = ThumbnailUtils.createVideoThumbnail(pathMain+imgYVid.getDirURI(), MediaStore.Video.Thumbnails.MICRO_KIND);
            }
            else if(imgYVid.getTipo()==MemoriaImgYVid.TYPE_IMG_URI){
                try {
                    bitmap = BitmapFactory.decodeFile(imgYVid.getDirURI());
                }
                catch (Exception e){
                    bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.message);
                }
            }
            else{
                bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.message);
            }
            imagePreviews[i].setImageBitmap(bitmap);
            imagePreviews[i].setVisibility(View.VISIBLE);

        }
        if(memoriaImgsYVids.size()<=3){
            expandImages.setVisibility(View.INVISIBLE);
        }
        else{
            expandImages.setVisibility(View.VISIBLE);
            expandImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int[] typearray=new int[memoriaImgsYVids.size()];
                    String[] array=new String[memoriaImgsYVids.size()];

                    for(int i=0;i<memoriaImgsYVids.size();i++){
                        array[i]=memoriaImgsYVids.get(i).getDirURI();
                        typearray[i]=memoriaImgsYVids.get(i).getTipo();
                    }
                    Intent intent = new Intent(activity, SeleccionDeImagenesActivity.class);
                    intent.putExtra("diruris",array);
                    intent.putExtra("tipos",typearray);
                    startActivityForResult(intent, 4);
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                MemoriaImgYVid img=new MemoriaImgYVid();
                img.setTipo(MemoriaImgYVid.TYPE_IMG_DIR);
                img.setDirURI(data.getStringExtra("file_name"));
                memoriaImgsYVids.add(img);
                setupPreviews();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
        else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                MemoriaImgYVid vid=new MemoriaImgYVid();
                vid.setTipo(MemoriaImgYVid.TYPE_VID_DIR);
                vid.setDirURI(data.getStringExtra("file_name"));
                memoriaImgsYVids.add(vid);
                setupPreviews();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
        else if(requestCode==3){
            String realPath="error";
            if(resultCode == RESULT_OK) {

                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                else
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                if(!realPath.equals("error")) {

                    MemoriaImgYVid img=new MemoriaImgYVid();
                    img.setTipo(MemoriaImgYVid.TYPE_IMG_URI);
                    img.setDirURI(realPath);
                    memoriaImgsYVids.add(img);
                    setupPreviews();
                }
                else {
                    setupPreviews();

                }

                /*MemoriaImgYVid img=new MemoriaImgYVid();
                img.setTipo(MemoriaImgYVid.TYPE_IMG_URI);
                img.setDirURI(realPath);
                memoriaImgsYVids.add(img);
                setupPreviews();*/
/*
                try {
                    //Log.d(TAG, String.valueOf(bitmap));
                    File f = new File(uri.getPath());

                    // Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());

                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        }
        else if(requestCode==4){
            if(resultCode==RESULT_OK){
                String[] dirUris=data.getStringArrayExtra("diruris");
                int[] tipos=data.getIntArrayExtra("tipos");
                memoriaImgsYVids.clear();
                for(int i=0;i<dirUris.length;i++){
                    MemoriaImgYVid memoriaImgYVid=new MemoriaImgYVid();
                    memoriaImgYVid.setTipo(tipos[i]);
                    memoriaImgYVid.setDirURI(dirUris[i]);
                    memoriaImgsYVids.add(memoriaImgYVid);
                }
                setupPreviews();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activity=this;
        setContentView(R.layout.activity_guardar_memoria);

        TextView description=(TextView)findViewById(R.id.description_agregar_memoria);
        description.setText("Agregar Memoria");

        takePictureButton=(ImageView)findViewById(R.id.takePictureButton);
        recordVideoButton=(ImageView)findViewById(R.id.recordVideoButton);
        attachFileButton=(ImageView)findViewById(R.id.attachFileButton);

        imagePreviews=new ImageView[]{(ImageView)findViewById(R.id.attachment1),(ImageView)findViewById(R.id.attachment2),
                (ImageView)findViewById(R.id.attachment3)};
        quickCancel=new ImageView[]{(ImageView)findViewById(R.id.cancelAB1),(ImageView)findViewById(R.id.cancelAB2),
                (ImageView)findViewById(R.id.cancelAB3)};
        for(int i=0;i<3;i++){
            imagePreviews[i].setVisibility(View.INVISIBLE);
            quickCancel[i].setVisibility(View.INVISIBLE);
            final int r=i;
            quickCancel[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    memoriaImgsYVids.remove(r);
                    setupPreviews();
                }
            });
        }


        expandImages=(ImageView)findViewById(R.id.expandListButton);
        expandImages.setVisibility(View.INVISIBLE);

        descripcionET=(EditText)findViewById(R.id.descripcionET);



        submitButton=(TextView) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Memoria memoria=new Memoria();
                memoria.setDescripcion(descripcionET.getText().toString());
                DatabaseHandler databaseHandler=new DatabaseHandler(activity.getApplicationContext());
                databaseHandler.insertMemoria(memoria);
                for(int i=0;i<memoriaImgsYVids.size();i++){
                    memoriaImgsYVids.get(i).setMemoria(databaseHandler.getMemorias().get(0).getId());
                    databaseHandler.insertImgVid(memoriaImgsYVids.get(i));
                }
                finish();
            }
        });
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CapturarFotomemoriaActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CapturarVideomemoriaActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        attachFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent imagenIntent = new Intent();
                imagenIntent.setType("image/*");
                imagenIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imagenIntent, "Seleccionar imagen"), 3);
            }
        });
        RelativeLayout cancelButton=(RelativeLayout) findViewById(R.id.backButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
