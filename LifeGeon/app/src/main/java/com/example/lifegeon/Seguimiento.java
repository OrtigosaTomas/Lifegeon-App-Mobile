package com.example.lifegeon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Seguimiento extends AppCompatActivity {

    private SqlHelper helperSql;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seguimiento);

        helperSql = new SqlHelper(this);
        barChart = findViewById(R.id.barChart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.botonCrearTarea).setOnClickListener(v -> abrirDialogoDiario());

        configurarGrafico();
    }

    private void configurarGrafico() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        
        SQLiteDatabase db = helperSql.getReadableDatabase();

        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String fechaBusqueda = sdf.format(cal.getTime());
            Cursor cursor = db.rawQuery("SELECT tareasCompletadas FROM seguimiento WHERE date(fecha) = ?", new String[]{fechaBusqueda});
            
            float completadas = 0;
            if (cursor.moveToFirst()) {
                completadas = cursor.getFloat(0);
            }
            entries.add(new BarEntry(i, completadas));
            cursor.close();
            
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tareas Completadas");
        dataSet.setColor(Color.parseColor("#FFB432"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.setFitBars(true);
        
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(diasSemana));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void abrirDialogoDiario() {
        SQLiteDatabase db = helperSql.getWritableDatabase();
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery("SELECT titulo, texto FROM diario WHERE date(fecha) = ?", new String[]{hoy});

        final EditText inputTitulo = new EditText(this);
        inputTitulo.setHint("Título de hoy");
        final EditText inputTexto = new EditText(this);
        inputTexto.setHint("¿Cómo ha ido el día?");
        inputTexto.setLines(5);

        if (cursor.moveToFirst()) {
            inputTitulo.setText(cursor.getString(0));
            inputTexto.setText(cursor.getString(1));
        }
        cursor.close();

        //Campos de texto
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 40, 60, 20);
        layout.addView(inputTitulo);
        layout.addView(inputTexto);

        new AlertDialog.Builder(this)
                .setTitle("Mi Diario")
                .setView(layout)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    String titulo = inputTitulo.getText().toString();
                    String texto = inputTexto.getText().toString();

                    Cursor c2 = db.rawQuery("SELECT id FROM diario WHERE date(fecha) = ?", new String[]{hoy});
                    if (c2.moveToFirst()) {
                        db.execSQL("UPDATE diario SET titulo = ?, texto = ? WHERE id = ?", 
                                new Object[]{titulo, texto, c2.getInt(0)});
                        Toast.makeText(this, "Entrada actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        db.execSQL("INSERT INTO diario (titulo, texto, fecha) VALUES (?, ?, datetime('now'))", 
                                new Object[]{titulo, texto});
                        Toast.makeText(this, "Entrada guardada", Toast.LENGTH_SHORT).show();
                    }
                    c2.close();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public static void registrarTareaCompletada(SqlHelper helperSql) {
        actualizarSeguimiento(helperSql, "tareasCompletadas");
    }

    public static void registrarEnemigoDerrotado(SqlHelper helperSql) {
        actualizarSeguimiento(helperSql, "enemigosDerrotados");
    }

    public static void registrarAventuraCompletada(SqlHelper helperSql) {
        actualizarSeguimiento(helperSql, "aventurasCompletadas");
    }

    private static void actualizarSeguimiento(SqlHelper helperSql, String columna) {
        SQLiteDatabase db = helperSql.getWritableDatabase();
        String hoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery("SELECT id, " + columna + " FROM seguimiento WHERE date(fecha) = ?", new String[]{hoy});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int valorActual = cursor.getInt(1);
            db.execSQL("UPDATE seguimiento SET " + columna + " = ? WHERE id = ?", new Object[]{valorActual + 1, id});
            cursor.close();
        } else {
            cursor.close();
            db.execSQL("INSERT INTO seguimiento (tareasCompletadas, enemigosDerrotados, aventurasCompletadas, fecha) VALUES (0, 0, 0, datetime('now', 'localtime'))");

            cursor = db.rawQuery("SELECT id, " + columna + " FROM seguimiento WHERE date(fecha) = ?", new String[]{hoy});
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                int valorActual = cursor.getInt(1);
                db.execSQL("UPDATE seguimiento SET " + columna + " = ? WHERE id = ?", new Object[]{valorActual + 1, id});
            }
            cursor.close();
        }
    }

    public void back(View view) {
        finish();
    }

    public void intentDiario(View view) {
        startActivity(new Intent(this, Diario.class));
    }

    public void intentStats(View view) {
        startActivity(new Intent(this, Stats.class));
    }
}
