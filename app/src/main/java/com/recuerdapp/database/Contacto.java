package com.recuerdapp.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by 79192 on 16/11/2017.
 */

public class Contacto {
    private int id;
    private String nombre;
    private String numero;
    private String parentesco;
    private String ruta;

    public Contacto() {
    }
    public Contacto(int id, String nombre, String numero, String parentesco,String ruta) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.parentesco = parentesco;
    }

    public Contacto(String nombre, String numero, String parentesco,String ruta) {
        this.nombre = nombre;
        this.numero = numero;
        this.parentesco = parentesco;
        this.ruta=ruta;
    }
    public Bitmap getContactoProfilePicture(){
        Bitmap bitmap=null;
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM";
        Date date=new Date();
        File dir=new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String name="/recuerdapp_contacto_"+String.valueOf(this.id)+".jpg";
        File file=new File(path+name);
        if(file.exists()){
            bitmap= BitmapFactory.decodeFile(path+name);
        }
        return bitmap;
    }
    public void setContactoProfilePicture(Bitmap bitmap){
        if(bitmap==null)
            return;
        OutputStream outStream = null;
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM";
        String name="/recuerdapp_contacto_"+String.valueOf(this.id)+".jpg";
        File file=new File(path+name);
        if(file.exists()){
            file.delete();
        }

        try {
            file.createNewFile();
            outStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public String getParentesco() {
        return parentesco;
    }

    public String getRuta() {
        return ruta;
    }

}
