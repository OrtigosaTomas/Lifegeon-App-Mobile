package com.example.lifegeon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EditarTarea extends AppCompatActivity {

    private SqlHelper helperSql;
    private TextView textoFecha, titulo, desc;
    private RadioGroup tipoGrupo;
    private RadioGroup dificultadGrupo;
    private Integer diaActual, mesActual, anioActual;
    private Integer dificultad, tipo, id;
    private String fecha;
    private Calendar calendar = Calendar.getInstance();
    private Tarea T = new Tarea("","",1,"",1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        helperSql =  new SqlHelper(this,"dbLifeGeon",null,1);

        titulo = findViewById(R.id.tituloTarea);
        desc = findViewById(R.id.descripcionTarea);
        textoFecha = findViewById(R.id.textoFecha);
        tipoGrupo = findViewById(R.id.grupoTipo);
        dificultadGrupo = findViewById(R.id.grupoDificultad);

        seleccionarTipo();
        seleccionarDificultad();

        anioActual = calendar.get(Calendar.YEAR);
        mesActual = calendar.get(Calendar.MONTH);
        diaActual = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);

        T.consultarTarea(helperSql, id);

    }

    public void seleccionarDificultad(){
        dificultadGrupo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.radioButton12){
                dificultad = 1;
            } else if (checkedId==R.id.radioButton13) {
                dificultad = 2;
            } else if (checkedId==R.id.radioButton14) {
                dificultad = 3;
            } else if (checkedId==R.id.radioButton15) {
                dificultad = 4;
            }
        });
    }

    public void seleccionarTipo(){
        tipoGrupo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.radioButton4){
                tipo = 1;
            } else if (checkedId==R.id.radioButton5) {
                tipo = 2;
            } else if (checkedId==R.id.radioButton6) {
                tipo = 3;
            } else if (checkedId==R.id.radioButton7) {
                tipo = 4;
            }
        });
    }

    public void seleccionarFecha(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditarTarea.this, (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
            fecha = dayOfMonth + "/" + (month+1) + "/" + year;
            textoFecha.setText(fecha);
        }, anioActual, mesActual, diaActual);
        datePickerDialog.show();
    }

    private void editarTarea(){
        if (desc.getText().toString().isEmpty()){
            desc.setText(" ");
        }
        if (textoFecha.getText().toString().isEmpty()){
            textoFecha.setText(" ");
        }
        if (titulo.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe rellenar el campo de Titulo",Toast.LENGTH_SHORT).show();
        } else {
            if (tipo == null){
                tipo = 1;
            }
            if (dificultad == null){
                dificultad = 1;
            }

            T.setTitulo(titulo.getText().toString());
            T.setDescripcion(desc.getText().toString());
            T.setFecha(textoFecha.getText().toString());
            T.setTipo(tipo);
            T.setDificultad(dificultad);
            T.actualizarTarea(helperSql);

            finish();
        }
    }

}