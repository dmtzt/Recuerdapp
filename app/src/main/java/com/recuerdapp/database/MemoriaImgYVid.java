package com.recuerdapp.database;

/**
 * Created by 79192 on 29/12/2017.
 */

public class MemoriaImgYVid {
    public final static int TYPE_IMG_DIR=0;
    public final static int TYPE_VID_DIR=1;
    public final static int TYPE_IMG_URI=2;
    public final static int TYPE_VID_URI=3;
    private Integer id;
    private Integer memoria;
    private Integer tipo;
    private String dirURI;
    public MemoriaImgYVid(int id,int memoria,int tipo,String dirURI){
        this.id=id;
        this.memoria=memoria;
        this.tipo=tipo;
        this.dirURI=dirURI;
    }
    public MemoriaImgYVid(){
        id=-1;
        memoria=-1;
        tipo=0;
        dirURI="";
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getDirURI() {
        return dirURI;
    }

    public void setDirURI(String dirURI) {
        this.dirURI = dirURI;
    }

    public Integer getMemoria() {
        return memoria;
    }

    public void setMemoria(Integer memoria) {
        this.memoria = memoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
