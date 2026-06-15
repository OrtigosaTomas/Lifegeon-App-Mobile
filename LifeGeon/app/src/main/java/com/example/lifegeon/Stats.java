package com.example.lifegeon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Stats extends AppCompatActivity {

    private SqlHelper helperSql;
    private LinearLayout contenedorStats;
    private TextView totalTareas, totalEnemigos, totalAventuras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        helperSql = new SqlHelper(this);
        contenedorStats = findViewById(R.id.contenedorStats);
        totalTareas = findViewById(R.id.totalTareas);
        totalEnemigos = findViewById(R.id.totalEnemigos);
        totalAventuras = findViewById(R.id.totalAventuras);

        consultarEstadisticas();
    }

    public void back(View view) {
        finish();
    }

    private void consultarEstadisticas() {
        if (contenedorStats != null) {
            contenedorStats.removeAllViews();
        }
        SQLiteDatabase db = helperSql.getReadableDatabase();

        Cursor cTotal = db.rawQuery("SELECT SUM(tareasCompletadas), SUM(enemigosDerrotados), SUM(aventurasCompletadas) FROM seguimiento", null);
        if (cTotal.moveToFirst()) {
            totalTareas.setText(String.valueOf(cTotal.getInt(0)));
            totalEnemigos.setText(String.valueOf(cTotal.getInt(1)));
            totalAventuras.setText(String.valueOf(cTotal.getInt(2)));
        }
        cTotal.close();

        Cursor c = db.rawQuery("SELECT date(fecha) as f, SUM(tareasCompletadas), SUM(enemigosDerrotados), SUM(aventurasCompletadas) FROM seguimiento GROUP BY f ORDER BY f DESC", null);

        if (c.moveToFirst()) {
            do {
                String fecha = c.getString(0);
                int tareas = c.getInt(1);
                int enemigos = c.getInt(2);
                int aventuras = c.getInt(3);

                CardView cardView = new CardView(this);
                cardView.setCardBackgroundColor(Color.parseColor("#6E372E"));
                
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(10, 10, 10, 10);
                cardView.setLayoutParams(cardParams);

                LinearLayout layoutVertical = new LinearLayout(this);
                layoutVertical.setOrientation(LinearLayout.VERTICAL);
                layoutVertical.setPadding(24, 24, 24, 24);

                TextView tvFecha = new TextView(this);
                String fechaLimpia = (fecha != null && fecha.length() >= 10) ? fecha.substring(0, 10) : fecha;
                tvFecha.setText(fechaLimpia);
                tvFecha.setTextColor(Color.parseColor("#FFB432"));
                tvFecha.setTextSize(18);
                tvFecha.setTypeface(null, android.graphics.Typeface.BOLD);

                TextView tvDetalle = new TextView(this);
                String detalle = "Tareas: " + tareas + " | Enemigos: " + enemigos + " | Aventuras: " + aventuras;
                tvDetalle.setText(detalle);
                tvDetalle.setTextColor(Color.WHITE);
                tvDetalle.setTextSize(16);
                tvDetalle.setPadding(0, 8, 0, 0);

                layoutVertical.addView(tvFecha);
                layoutVertical.addView(tvDetalle);
                cardView.addView(layoutVertical);
                
                contenedorStats.addView(cardView);

            } while (c.moveToNext());
        }
        c.close();
    }
}
