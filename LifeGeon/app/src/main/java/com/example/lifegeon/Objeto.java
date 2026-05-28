package com.example.lifegeon;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

public class Objeto {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer precio;

    public Objeto(Integer id, String nombre, String descripcion, Integer precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPrecio() { return precio; }

    public void setPrecio(Integer precio) { this.precio = precio; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public void comprarObjeto(SqlHelper S){
        SQLiteDatabase db = S.getWritableDatabase();

        ContentValues valores = new ContentValues();
        Usuario User = new Usuario(0,0);
        User.consultarUsuario(S);
        User.quitarRecompensa(precio,"monedas",S);
        valores.put("cantidadObjeto",1);
        valores.put("objeto_id",id);
        db.insert("inventario",null,valores);
    }

    public void adquirirObjeto(SqlHelper S, boolean positivo){
        SQLiteDatabase db = S.getWritableDatabase();
        Cursor C = db.rawQuery("SELECT cantidadObjeto FROM inventario WHERE id = "+ id,null);
        if (C.moveToFirst()){
            ContentValues valores = new ContentValues();
            int cantObj = C.getInt(0);
            if (positivo){
                cantObj = cantObj + 1;
            } else {
                cantObj = cantObj - 1;
            }
            valores.put("cantidadObjeto",cantObj);
            db.update("inventario",valores,"id="+ id,null);
        }
        C.close();
    }

}
