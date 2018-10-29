package com.recuerdapp.recuerdapp;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recuerdapp.database.MemoriaImgYVid;
import com.recuerdapp.image_grid.GridViewAdapter;
import com.recuerdapp.image_grid.Image;

import java.util.ArrayList;

/**
 * Created by 79192 on 31/12/2017.
 */

public class SeleccionDeImagenesActivity extends AppCompatActivity{
    private ArrayList<MemoriaImgYVid> memoriaImgYVids;
    private ArrayList<Integer> removes=new ArrayList<Integer>();
    private GridViewAdapter gridAdapter;


    private int[] typearray;
    String[] array;
    private ArrayList<Image> getMemoryThumbnails(ContentResolver resolver) {
        ArrayList<Image> imageItems=new ArrayList<>();
        for (int i = 0; i <memoriaImgYVids.size(); i++) {
            // Bitmap bitmap=BitmapFactory.
            Bitmap bitmap=null;
            bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.message);
            bitmap.setDensity(5);
            if(memoriaImgYVids.get(i).getTipo()==MemoriaImgYVid.TYPE_IMG_DIR){

                String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";

                bitmap = BitmapFactory.decodeFile(pathMain+memoriaImgYVids.get(i).getDirURI());

            }
            else if(memoriaImgYVids.get(i).getTipo()==MemoriaImgYVid.TYPE_IMG_URI){
                try {
                    bitmap = BitmapFactory.decodeFile(memoriaImgYVids.get(i).getDirURI());
                } catch (Exception e) {

                }
            }
            else if(memoriaImgYVids.get(i).getTipo()==MemoriaImgYVid.TYPE_VID_DIR){
                String pathMain= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM/";
                bitmap = ThumbnailUtils.createVideoThumbnail(pathMain+memoriaImgYVids.get(i).getDirURI(), MediaStore.Video.Thumbnails.MINI_KIND);
            }
            imageItems.add(new Image(bitmap, i));
        }
        return imageItems;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_img);
        memoriaImgYVids=new ArrayList<MemoriaImgYVid>();
        Intent intent=getIntent();
        String[] dirUris=intent.getStringArrayExtra("diruris");
        int[] tipos=intent.getIntArrayExtra("tipos");
        for(int i=0;i<dirUris.length;i++){
            MemoriaImgYVid memoriaImgYVid=new MemoriaImgYVid();
            memoriaImgYVid.setTipo(tipos[i]);
            memoriaImgYVid.setDirURI(dirUris[i]);
            memoriaImgYVids.add(memoriaImgYVid);
        }

        final GridView memoryView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.img_vid_layout, getMemoryThumbnails(getContentResolver()));
        memoryView.setAdapter(gridAdapter);

        memoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                adb.setNegativeButton("Cancelar", null);
                final int m= (int) id;
                adb.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removes.add(m);
                        gridAdapter.remove(gridAdapter.getItem(m));
                    }
                });
                adb.setTitle("Elminar");
                adb.show();
            }
        });

        RelativeLayout cancelButton=(RelativeLayout) findViewById(R.id.backButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView submitButton=(TextView) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data=new Intent();


                typearray=new int[gridAdapter.getCount()];
                array=new String[gridAdapter.getCount()];
                System.out.println(gridAdapter.getCount());
                for(int i=0;i<gridAdapter.getCount();i++){
                    array[i]=memoriaImgYVids.get(gridAdapter.getItem(i).getIdx()).getDirURI();
                    typearray[i]=memoriaImgYVids.get(gridAdapter.getItem(i).getIdx()).getTipo();
                }
                data.putExtra("diruris",array);
                data.putExtra("tipos",typearray);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

}
