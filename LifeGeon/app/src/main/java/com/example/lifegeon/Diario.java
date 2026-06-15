package com.example.lifegeon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Diario extends AppCompatActivity {

    private LinearLayout contenedorDiario;
    private SqlHelper helperSql;
    private TextView tituloDetalle, textoDetalle;
    private CardView selectedCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diario);

        helperSql = new SqlHelper(this);

        tituloDetalle = findViewById(R.id.tituloDetalle);
        textoDetalle = findViewById(R.id.textoDetalle);
        contenedorDiario = findViewById(R.id.contenedorDiario);

        consultarDiario();
    }

    public void back(View view) {
        finish();
    }

    public void consultarDiario() {
        if (contenedorDiario == null) return;
        contenedorDiario.removeAllViews();
        SQLiteDatabase db = helperSql.getReadableDatabase();

        try (Cursor c = db.rawQuery("SELECT id, titulo, texto, fecha FROM diario ORDER BY fecha DESC", null)) {

            if (c != null && c.moveToFirst()) {
                do {
                    int id = c.getInt(0);
                    String titulo = c.getString(1);
                    if (titulo == null) titulo = "Sin título";
                    String texto = c.getString(2);
                    if (texto == null) texto = "";
                    String fecha = c.getString(3);

                    CardView cardView = new CardView(this);
                    cardView.setCardBackgroundColor(Color.parseColor("#6E372E"));

                    LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cardParams.setMargins(10, 10, 10, 10);
                    cardView.setLayoutParams(cardParams);

                    final String fTitulo = titulo;
                    final String fTexto = texto;
                    cardView.setOnClickListener(v -> {
                        if (selectedCardView != null) {
                            selectedCardView.setCardBackgroundColor(Color.parseColor("#6E372E"));
                        }
                        selectedCardView = cardView;
                        cardView.setCardBackgroundColor(Color.parseColor("#562828"));

                        if (tituloDetalle != null) tituloDetalle.setText(fTitulo);
                        if (textoDetalle != null) textoDetalle.setText(fTexto);
                    });

                    LinearLayout layoutHorizontal = new LinearLayout(this);
                    layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                    layoutHorizontal.setPadding(20, 20, 20, 20);

                    LinearLayout layoutVertical = new LinearLayout(this);
                    layoutVertical.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    layoutVertical.setLayoutParams(vParams);

                    TextView tvTitulo = new TextView(this);
                    String fechaLimpia = "Sin fecha";
                    if (fecha != null && fecha.length() >= 10) {
                        fechaLimpia = fecha.substring(0, 10);
                    } else if (fecha != null) {
                        fechaLimpia = fecha;
                    }
                    String tvt = titulo + " (" + fechaLimpia + ")";
                    tvTitulo.setText(tvt);
                    tvTitulo.setTextColor(Color.WHITE);
                    tvTitulo.setTextSize(18);

                    TextView tvTexto = new TextView(this);
                    tvTexto.setText(texto);
                    tvTexto.setTextColor(Color.parseColor("#CCCCCC"));
                    tvTexto.setMaxLines(2);

                    layoutVertical.addView(tvTitulo);
                    layoutVertical.addView(tvTexto);

                    ImageButton btnEditar = new ImageButton(this);
                    btnEditar.setImageResource(R.drawable.lapiz);
                    btnEditar.setBackgroundColor(Color.parseColor("#874734"));
                    btnEditar.setPadding(20, 20, 20, 20);
                    btnEditar.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(120, 120);
                    btnEditar.setLayoutParams(btnParams);

                    btnEditar.setOnClickListener(v -> abrirDialogoEdicion(id, fTitulo, fTexto));

                    layoutHorizontal.addView(layoutVertical);
                    layoutHorizontal.addView(btnEditar);

                    cardView.addView(layoutHorizontal);
                    contenedorDiario.addView(cardView);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirDialogoEdicion(int id, String tituloActual, String textoActual) {
        final EditText inputTitulo = new EditText(this);
        inputTitulo.setText(tituloActual);
        final EditText inputTexto = new EditText(this);
        inputTexto.setText(textoActual);
        inputTexto.setLines(5);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 20);
        layout.addView(inputTitulo);
        layout.addView(inputTexto);

        new AlertDialog.Builder(this)
                .setTitle("Editar Entrada")
                .setView(layout)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    SQLiteDatabase db = helperSql.getWritableDatabase();
                    db.execSQL("UPDATE diario SET titulo = ?, texto = ? WHERE id = ?", 
                            new Object[]{inputTitulo.getText().toString(), inputTexto.getText().toString(), id});
                    Toast.makeText(this, "Entrada actualizada", Toast.LENGTH_SHORT).show();
                    consultarDiario();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
