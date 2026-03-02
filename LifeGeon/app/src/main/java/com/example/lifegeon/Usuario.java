package com.example.lifegeon;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Usuario {

    private Integer dados;
    private Integer monedas;

    public Usuario(Integer dados, Integer monedas) {
        this.dados = dados;
        this.monedas = monedas;
    }

    public Integer getMonedas() {
        return monedas;
    }

    public void setMonedas(Integer monedas) {
        this.monedas = monedas;
    }

    public Integer getDados() {
        return dados;
    }

    public void setDados(Integer dados) {
        this.dados = dados;
    }

    public void obtenerRecompensa(int cantidad, String tipo, SqlHelper S){
        SQLiteDatabase db = S.getWritableDatabase();

        ContentValues valores = new ContentValues();
        if (tipo.equalsIgnoreCase("monedas")){
            monedas += cantidad;
            valores.put("monedas",monedas);
        } else if (tipo.equalsIgnoreCase("dados")){
            dados += cantidad;
            valores.put("dados",dados);
        } else {
            Log.e("error","Error al elegir tipo de recompensa");
        }

        db.update("usuarios",valores,"id=1",null);
        db.close();
    }

    public void consultarUsuario(SqlHelper S){

        SQLiteDatabase db = S.getWritableDatabase();

        Cursor C = db.rawQuery("SELECT monedas, dados FROM usuarios",null);

        if (C.moveToFirst()){
            setMonedas(C.getInt(0));
            setDados(C.getInt(1));
        }

        C.close();
        db.close();

    }

}
