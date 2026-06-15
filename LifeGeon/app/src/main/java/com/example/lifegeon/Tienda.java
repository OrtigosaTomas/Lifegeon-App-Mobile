package com.example.lifegeon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Tienda extends AppCompatActivity {

    private LinearLayout contTienda;
    private SqlHelper helperSql;
    private ImageView imagen;
    private Usuario User = new Usuario(0,0);
    private TextView monedasTexto;
    private CardView selectedCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        helperSql = new SqlHelper(this);

        imagen = findViewById(R.id.imageView);
        monedasTexto = findViewById(R.id.monedasText);
        contTienda = findViewById(R.id.contienda);
        consultarTienda();

        User.consultarUsuario(helperSql);
        monedasTexto.setText(String.valueOf(User.getMonedas()));
    }

    public void back(View view) {
        finish();
    }

    public void consultarTienda(){

        SQLiteDatabase db = helperSql.getWritableDatabase();

        Cursor C = db.rawQuery("SELECT id, nombre, descripcion, precio FROM objetos",null);

        if (C.moveToFirst()){

            do {
                Integer id = C.getInt(0);
                String nombre = C.getString(1);
                String descripcion = C.getString(2);
                Integer precio = C.getInt(3);

                Log.i("info", "creando Tarjeta..." + id + " " + nombre + " " + descripcion + " " + precio);

                Objeto objetoActual = new Objeto(id,nombre,descripcion,precio);

                CardView cardView = new CardView(Tienda.this);
                cardView.setCardBackgroundColor(Color.parseColor("#6E372E"));
                cardView.setMinimumHeight(300);

                CardView.LayoutParams cardViewParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, 300);
                cardViewParams.setMargins(0, 10, 0, 10);
                cardView.setLayoutParams(cardViewParams);

                cardView.setOnClickListener(view -> {
                    if (selectedCardView != null) {
                        selectedCardView.setCardBackgroundColor(Color.parseColor("#6E372E"));
                    }
                    selectedCardView = cardView;
                    cardView.setCardBackgroundColor(Color.parseColor("#562828"));

                    int resId = getResources().getIdentifier("o" + id, "drawable", getPackageName());

                    if (resId != 0) {
                        imagen.setImageResource(resId);
                    } else {
                        imagen.setImageResource(R.drawable.o1);
                    }
                });

                TextView tituloObjeto = new TextView(Tienda.this);
                TextView descripcionObjeto = new TextView(Tienda.this);
                TextView precioObjeto = new TextView(Tienda.this);

                tituloObjeto.setText(objetoActual.getNombre());
                descripcionObjeto.setText(objetoActual.getDescripcion());
                precioObjeto.setText(String.valueOf(objetoActual.getPrecio()));

                tituloObjeto.setTextColor(Color.parseColor("#FFFFFF"));
                descripcionObjeto.setTextColor(Color.parseColor("#FFFFFF"));
                precioObjeto.setTextColor(Color.parseColor("#FFFFFF"));

                tituloObjeto.setTextSize(20);
                descripcionObjeto.setTextSize(20);
                precioObjeto.setTextSize(20);

                ImageButton imageButton = new ImageButton(Tienda.this);

                imageButton.setOnClickListener(view -> {
                    int resId = getResources().getIdentifier("o" + id, "drawable", getPackageName());

                    if (resId != 0) {
                        imagen.setImageResource(resId);
                    } else {
                        imagen.setImageResource(R.drawable.o1);
                    }
                    if(User.getMonedas() >= objetoActual.getPrecio()){
                        new AlertDialog.Builder(Tienda.this)
                                .setTitle("Confirmar compra")
                                .setMessage("¿Quieres comprar " + objetoActual.getNombre() + " por " + objetoActual.getPrecio() + " monedas?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    objetoActual.comprarObjeto(helperSql);
                                    User.consultarUsuario(helperSql);
                                    monedasTexto.setText(String.valueOf(User.getMonedas()));
                                    Toast.makeText(this, "Compra realizada", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("No", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(Tienda.this)
                                .setTitle("Monedas insuficientes")
                                .setMessage("No tienes suficientes monedas para comprar este objeto.")
                                .setPositiveButton("Aceptar", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
                    }
                });

                LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(300, 300);
                imageButton.setLayoutParams(imageButtonParams);

                imageButton.setBackgroundColor(Color.parseColor("#874734"));

                LinearLayout linearLayoutVertical = new LinearLayout(Tienda.this);
                linearLayoutVertical.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                linearLayoutParams.setMargins(16, 16, 16, 16);
                linearLayoutVertical.setLayoutParams(linearLayoutParams);

                linearLayoutVertical.addView(tituloObjeto);
                linearLayoutVertical.addView(descripcionObjeto);
                linearLayoutVertical.addView(precioObjeto);

                LinearLayout linearLayoutHorizontal = new LinearLayout(Tienda.this);
                linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams horizontalParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayoutHorizontal.setLayoutParams(horizontalParams);

                linearLayoutHorizontal.addView(linearLayoutVertical);
                linearLayoutHorizontal.addView(imageButton);

                cardView.addView(linearLayoutHorizontal);

                contTienda.addView(cardView);
            } while (C.moveToNext());

        }

        C.close();
        db.close();
    }

}