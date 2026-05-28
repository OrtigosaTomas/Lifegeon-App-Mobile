package com.example.lifegeon;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Tarea {

    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer tipo;
    private String fecha;
    private Integer dificultad;

    public Tarea(String titulo, String descripcion, Integer tipo, String fecha, Integer dificultad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.dificultad = dificultad;
    }

    public Tarea(Integer id, String titulo, String descripcion, Integer tipo, String fecha, Integer dificultad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.dificultad = dificultad;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getDificultad() {
        return dificultad;
    }
    public void setDificultad(Integer dificultad) {
        this.dificultad = dificultad;
    }

    public Integer getTipo() {
        return tipo;
    }
    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public void eliminarTarea(SqlHelper S, Integer id){
        SQLiteDatabase db = S.getWritableDatabase();
        db.delete("tareas",id+ " = id",null);
    }

    public void guardarTarea(SqlHelper S){

        SQLiteDatabase db = S.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("titulo",titulo);
        valores.put("descripcion",descripcion);
        valores.put("tipo",tipo);
        valores.put("fecha",fecha);
        valores.put("dificultad",dificultad);
        long idNuevo = db.insert("tareas",null,valores);
        setId((int) idNuevo);
    }

    public void actualizarTarea(SqlHelper S){

        SQLiteDatabase db = S.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("titulo",titulo);
        valores.put("descripcion",descripcion);
        valores.put("fecha",fecha);
        valores.put("dificultad",dificultad);
        db.update("tareas",valores,"id="+id,null);
    }

    public void consultarTarea(SqlHelper S, Integer i){
        SQLiteDatabase db = S.getWritableDatabase();

        Cursor C = db.rawQuery("SELECT titulo,descripcion,tipo,fecha,dificultad FROM tareas WHERE id ="+ i ,null);

        if (C.moveToFirst()){
            setTitulo(C.getString(0));
            setDescripcion(C.getString(1));
            setTipo(C.getInt(2));
            setFecha(C.getString(3));
            setDificultad(C.getInt(4));
        }

        C.close();
    }


}
