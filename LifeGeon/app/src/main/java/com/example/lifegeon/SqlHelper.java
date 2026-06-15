package com.example.lifegeon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbLifeGeon";
    private static final int DATABASE_VERSION = 2;

    public SqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE objetos(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, descripcion TEXT, precio INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, monedas INTEGER DEFAULT 0, dados INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE seguimiento(id INTEGER PRIMARY KEY AUTOINCREMENT, tareasCompletadas INTEGER DEFAULT 0, enemigosDerrotados INTEGER DEFAULT 0, aventurasCompletadas INTEGER DEFAULT 0, fecha DATETIME)");
        db.execSQL("CREATE TABLE personaje(id INTEGER PRIMARY KEY AUTOINCREMENT, vida INTEGER DEFAULT 0, mana INTEGER DEFAULT 0, personaje INTEGER DEFAULT 0, mascota INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE inventario(id INTEGER PRIMARY KEY AUTOINCREMENT, cantidadObjeto INTEGER DEFAULT 0, objeto_id INTEGER, FOREIGN KEY (objeto_id) REFERENCES objetos(id))");
        db.execSQL("CREATE TABLE tareas(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT ,descripcion TEXT, tipo INTEGER, fecha TEXT, dificultad INTEGER)");
        db.execSQL("CREATE TABLE diario(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, texto TEXT, fecha DATETIME)");
        db.execSQL("INSERT INTO usuarios(monedas,dados) VALUES (0,0)");
        db.execSQL("INSERT INTO personaje(vida,mana,personaje,mascota) VALUES (100,100,1,1)");
        db.execSQL("INSERT INTO objetos(nombre,descripcion,precio) VALUES ('Poción de vida','restaura 10 puntos de vida',10)");
        db.execSQL("INSERT INTO objetos(nombre,descripcion,precio) VALUES ('Poción de magia','restaura 10 puntos de magia',10)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS objetos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS seguimiento");
        db.execSQL("DROP TABLE IF EXISTS personaje");
        db.execSQL("DROP TABLE IF EXISTS inventario");
        db.execSQL("DROP TABLE IF EXISTS tareas");
        db.execSQL("DROP TABLE IF EXISTS diario");
        onCreate(db);
    }

}

