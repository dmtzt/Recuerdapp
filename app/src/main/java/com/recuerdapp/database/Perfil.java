package com.recuerdapp.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by 79192 on 16/11/2017.
 */

public class   Perfil {
    private String nombre;
    private Calendar fechaNacimiento;
    private String contactoDirectoT;
    private String contactoDirectoN;
    private String tipoSanguineo;
    private String alergias;
    private String antecedentesPatologicos;
    private String antecedentesHeredofamiliares;
    private String medicamentosYHorario;
    private String genero;
    private String ruta;

    private String dirCalle;
    private String dirCol;
    private String dirNum;
    private String dirEstado;
    private String dirCiudad;



    public String getNombre() {
        return nombre;
    }

    public static Bitmap getProfilePicture(){
        Bitmap bitmap=null;
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM";
        Date date=new Date();
        File dir=new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file=new File(path+"/recuerdapp_profile.jpg");
        if(file.exists()){
            bitmap= BitmapFactory.decodeFile(path+"/recuerdapp_profile.jpg");
        }
        return bitmap;
    }
    public static void setProfile(Bitmap bitmap){
        if(bitmap==null)
            return;
        OutputStream outStream = null;
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/com.recuerdapp/DCIM";
        File file=new File(path+"/recuerdapp_profile.jpg");
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
    public int getEdad() {
        if(fechaNacimiento==null)
            return 0;
        Calendar date = Calendar.getInstance();
        int edad=date.get(Calendar.YEAR)-fechaNacimiento.get(Calendar.YEAR)-1;
        if(fechaNacimiento.get(Calendar.MONTH)<date.get(Calendar.MONTH)||
                (fechaNacimiento.get(Calendar.MONTH)==date.get(Calendar.MONTH)&&fechaNacimiento.get(Calendar.DATE)<=date.get(Calendar.DATE))){
            edad++;
        }
        return edad;
    }

    public String getDomicilio() {
        return dirCalle+" #"+ dirNum+", "+dirCol+
                ", "+dirEstado+", "+dirCiudad;
    }

    public Calendar getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getContactoDirectoT() {
        return contactoDirectoT;
    }

    public String getContactoDirectoN() {
        return contactoDirectoN;
    }

    public String getAntecedentesPatologicos() {
        return antecedentesPatologicos;
    }

    public String getAntecedentesHeredofamiliares() {
        return antecedentesHeredofamiliares;
    }

    public String getMedicamentosYHorario() {
        return medicamentosYHorario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaNacimiento(Calendar fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }
    public String getTipoSanguineo() {
        return tipoSanguineo;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public String getGenero() {
        return genero;
    }

    public String getRuta() {
        return ruta;
    }
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }
    public String getAlergias() {
        return alergias;
    }


    public void setContactoDirectoT(String contactoDirectoT) {
        this.contactoDirectoT = contactoDirectoT;
    }
    public void setContactoDirectoN(String contactoDirectoN) {
        this.contactoDirectoN = contactoDirectoN;
    }

    public void setAntecedentesPatologicos(String antecedentesPatologicos) {
        this.antecedentesPatologicos = antecedentesPatologicos;
    }

    public void setAntecedentesHeredofamiliares(String antecedentesHeredofamiliares) {
        this.antecedentesHeredofamiliares = antecedentesHeredofamiliares;
    }

    public void setMedicamentosYHorario(String medicamentosYHorario) {
        this.medicamentosYHorario = medicamentosYHorario;
    }

    public void setDirCalle(String dirCalle) {
        this.dirCalle = dirCalle;
    }

    public void setDirCol(String dirCol) {
        this.dirCol = dirCol;
    }

    public void setDirNum(String dirNum) {
        this.dirNum = dirNum;
    }

    public void setDirEstado(String dirEstado) {
        this.dirEstado = dirEstado;
    }

    public void setDirCiudad(String dirCiudad) {
        this.dirCiudad = dirCiudad;
    }

    public String getDirCalle() {
        return dirCalle;
    }

    public String getDirCol() {
        return dirCol;
    }

    public String getDirNum() {
        return dirNum;
    }

    public String getDirEstado() {
        return dirEstado;
    }

    public String getDirCiudad() {
        return dirCiudad;
    }
}

