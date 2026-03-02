package com.example.lifegeon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tienda extends AppCompatActivity {

    private LinearLayout contTienda;
    private SqlHelper helperSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        contTienda = findViewById(R.id.contienda);

    }

    public void consultarTienda(){

        SQLiteDatabase db = helperSql.getWritableDatabase();

        Cursor C = db.rawQuery("SELECT id, nombre, descripcion, precio FROM objetos",null);
        Objeto O ;

        if (C.moveToFirst()){

            do {

                Integer id = C.getColumnIndex("id");
                String nombre = String.valueOf(C.getColumnIndex("nombre"));
                String descripcion = String.valueOf(C.getColumnIndex("descripcion"));
                Integer precio = C.getColumnIndex("precio");

                O = new Objeto(id,nombre,descripcion,precio);

                CardView cardView = new CardView(Tienda.this);
                cardView.setCardBackgroundColor(Color.parseColor("#653D2D"));
                cardView.setMinimumHeight(300);

                CardView.LayoutParams cardViewParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, 300);
                cardViewParams.setMargins(0,10,0,10);
                cardView.setLayoutParams(cardViewParams);

                TextView tituloObjeto = new TextView(Tienda.this);
                TextView descripcionObjeto = new TextView(Tienda.this);
                TextView precioObjeto = new TextView(Tienda.this);

                tituloObjeto.setText(O.getNombre());
                descripcionObjeto.setText(O.getDescripcion());
                precioObjeto.setText(O.getPrecio());

                tituloObjeto.setText(O.getNombre());
                descripcionObjeto.setText(O.getDescripcion());
                precioObjeto.setText(O.getPrecio());

                tituloObjeto.setTextColor(Color.parseColor("#FFFFFF"));
                descripcionObjeto.setTextColor(Color.parseColor("#FFFFFF"));
                precioObjeto.setTextColor(Color.parseColor("#FFFFFF"));

                tituloObjeto.setTextSize(20);
                descripcionObjeto.setTextSize(20);
                precioObjeto.setTextSize(20);

                contTienda.addView(cardView);
            } while (C.moveToNext());

        }

        C.close();
        db.close();
    }

}