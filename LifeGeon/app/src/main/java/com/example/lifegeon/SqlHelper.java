package com.example.lifegeon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {

    public SqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE objetos(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, descripcion TEXT, precio INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, monedas INTEGER DEFAULT 0, dados INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE seguimiento(id INTEGER PRIMARY KEY AUTOINCREMENT, tareasCompletadas INTEGER DEFAULT 0, enemigosDerrotados INTEGER DEFAULT 0, aventurasCompletadas INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE personaje(id INTEGER PRIMARY KEY AUTOINCREMENT, vida INTEGER DEFAULT 0, mana INTEGER DEFAULT 0, personaje INTEGER DEFAULT 0, mascota INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE inventario(id INTEGER PRIMARY KEY AUTOINCREMENT, cantidadObjeto INTEGER DEFAULT 0, objeto_id INTEGER, FOREIGN KEY (objeto_id) REFERENCES objetos(id))");
        db.execSQL("CREATE TABLE tareas(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT ,descripcion TEXT, tipo INTEGER, fecha TEXT, dificultad INTEGER)");
        db.execSQL("INSERT INTO usuarios(monedas,dados) VALUES (0,0)");
        db.execSQL("INSERT INTO personaje(vida,mana,personaje,mascota) VALUES (100,100,1,1)");
        db.execSQL("INSERT INTO objetos(nombre,descripcion,precio) VALUES ('Poción de vida','restaura 10 puntos de vida',10)");
        db.execSQL("INSERT INTO objetos(nombre,descripcion,precio) VALUES ('Poción de magia','restaura 10 puntos de magia',10)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}

