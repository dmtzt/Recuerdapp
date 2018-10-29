package com.recuerdapp.database;


import java.util.Calendar;

/**
 * Created by 79192 on 16/11/2017.
 */

public class Memoria {
    private Integer id;
    private Calendar fecha_hora;
    private String nombre;
    private Integer formato;
    private String descripcion;
    private String ruta;

    public Memoria() {
        nombre="";
        descripcion="";
        formato=0;
        descripcion="";
        ruta="";
        id=-1;
    }

    public Memoria(String nombre, Integer formato, String descripcion, String ruta) {
        this.fecha_hora = fecha_hora;
        this.nombre = nombre;
        this.formato = formato;
        this.descripcion = descripcion;
        this.ruta = ruta;
    }

    public Memoria(Integer id, Calendar fecha_hora, String nombre, Integer formato, String descripcion, String ruta) {
        this.id = id;
        this.fecha_hora = fecha_hora;
        this.nombre = nombre;
        this.formato = formato;
        this.descripcion = descripcion;
        this.ruta = ruta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Calendar fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFormato() {
        return formato;
    }

    public void setFormato(Integer formato) {
        this.formato = formato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
