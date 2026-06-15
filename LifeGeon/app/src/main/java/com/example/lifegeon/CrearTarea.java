package com.example.lifegeon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CrearTarea extends AppCompatActivity {

    private Integer dificultad, tipo;
    private Calendar calendar = Calendar.getInstance();
    private Integer diaActual, mesActual, anioActual;
    private RadioGroup tipoGrupo;
    private RadioGroup dificultadGrupo;
    private TextView textoFecha, titulo, desc;
    private SqlHelper helperSql;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        helperSql = new SqlHelper(this);

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
            } else {
                dificultad = 1;
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
            } else {
                tipo = 1;
            }
        });
    }

    public void seleccionarFecha(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(CrearTarea.this, (DatePickerDialog.OnDateSetListener) (view1, year, month, dayOfMonth) -> {
            fecha = dayOfMonth + "/" + (month+1) + "/" + year;
            textoFecha.setText(fecha);
        }, anioActual, mesActual, diaActual);
        datePickerDialog.show();
    }

    public void crearTarea(View view){
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
            Intent intent = new Intent(CrearTarea.this, Menu.class);
            intent.putExtra("datoTitulo", titulo.getText().toString());
            intent.putExtra("datoDescripcion", desc.getText().toString());
            intent.putExtra("datoTipo", tipo);
            intent.putExtra("datoFechaLimite", textoFecha.getText().toString());
            intent.putExtra("datoDificultad", dificultad);

            Tarea T = new Tarea(titulo.getText().toString(),desc.getText().toString(),tipo,textoFecha.getText().toString(),dificultad);
            T.guardarTarea(helperSql);
            intent.putExtra("id",T.getId());
            setResult(RESULT_OK, intent);

            finish();
        }

    }

}