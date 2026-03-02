package com.example.lifegeon;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    private TextView monedas,dados;
    private ActivityResultLauncher<Intent> crearTarjetaLauncher;
    private LinearLayout contenedorTarea1,contenedorTarea2,contenedorTarea3,contenedorTarea4;
    private RadioGroup tipoGrupo;
    private Integer tipoTarea, idTarea;
    private HorizontalScrollView tipoScroll;
    private SqlHelper helperSql;
    private Usuario User = new Usuario(0,0);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        helperSql =  new SqlHelper(this,"dbLifeGeon",null,1);

        monedas = findViewById(R.id.monedasNumb);
        dados = findViewById(R.id.dadosNumb);
        contenedorTarea1 = findViewById(R.id.contenedorTareas1);
        contenedorTarea2 = findViewById(R.id.contenedorTareas2);
        contenedorTarea3 = findViewById(R.id.contenedorTareas3);
        contenedorTarea4 = findViewById(R.id.contenedorTareas4);
        tipoGrupo = findViewById(R.id.tipoGrupoMenu);
        tipoScroll = findViewById(R.id.tipoScroll);
        seleccionarTipo();

        tipoScroll.setOnTouchListener((v, event) -> true);

        crearTarjetaLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int id = data.getIntExtra("id", -1);
                            String titulo = data.getStringExtra("datoTitulo");
                            String descripcion = data.getStringExtra("datoDescripcion");
                            Integer tipo = data.getIntExtra("datoTipo", 1);
                            String fecha = data.getStringExtra("datoFechaLimite");
                            Integer dificultad = data.getIntExtra("datoDificultad", 1);

                            Tarea T = new Tarea(id,titulo, descripcion, tipo, fecha, dificultad);
                            crearTarjeta(T);
                        }
                    }
                });

        consultarTareas();
        User.consultarUsuario(helperSql);
        monedas.setText(String.valueOf(User.getMonedas()));
        dados.setText(String.valueOf(User.getDados()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        contenedorTarea1.removeAllViews();
        contenedorTarea2.removeAllViews();
        contenedorTarea3.removeAllViews();
        contenedorTarea4.removeAllViews();
        consultarTareas();
        User.consultarUsuario(helperSql);
        monedas.setText(String.valueOf(User.getMonedas()));
        dados.setText(String.valueOf(User.getDados()));
    }

    public void crearTarjeta(Tarea T) {

        CardView cardView = new CardView(Menu.this);
        cardView.setCardBackgroundColor(Color.parseColor("#653D2D"));
        cardView.setMinimumHeight(300);
        cardView.setOnClickListener(view -> {
            IntentEditarTarea(T);
        });

        CardView.LayoutParams cardViewParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, 300);
        cardViewParams.setMargins(0,10,0,10);
        cardView.setLayoutParams(cardViewParams);

        RelativeLayout relativeLayout = new RelativeLayout(Menu.this);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(relativeLayoutParams);

        ImageButton imageButton = new ImageButton(Menu.this);
        ImageButton imageButton2 = new ImageButton(Menu.this);
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams imageButtonParams2 = new RelativeLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT);

        if (T.getTipo() == 1){
            imageButton.setBackgroundColor(Color.parseColor("#714933"));
            imageButton.setOnClickListener(view -> {
                contenedorTarea1.removeView(cardView);
                T.eliminarTarea(helperSql,T.getId());
                User.consultarUsuario(helperSql);
                User.obtenerRecompensa(10,"monedas",helperSql);
                User.obtenerRecompensa(1,"dados",helperSql);
                monedas.setText(String.valueOf(User.getMonedas()));
                dados.setText(String.valueOf(User.getDados()));
            });
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageButtonParams.width = 300;
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            imageButtonParams.setMargins(0, 0, 0, 0);

        } else if (T.getTipo() == 2){
            imageButton.setBackgroundColor(Color.parseColor("#714933"));
            imageButton.setOnClickListener(view -> {
                User.consultarUsuario(helperSql);
                User.obtenerRecompensa(10,"monedas",helperSql);
                User.obtenerRecompensa(1,"dados",helperSql);
                monedas.setText(String.valueOf(User.getMonedas()));
                dados.setText(String.valueOf(User.getDados()));
            });
            imageButton2.setOnClickListener(view -> {
                User.consultarUsuario(helperSql);
                User.obtenerRecompensa(-1,"monedas",helperSql); //negativo
                monedas.setText(String.valueOf(User.getMonedas()));
                dados.setText(String.valueOf(User.getDados()));
            });
            imageButton2.setBackgroundColor(Color.parseColor("#714933"));
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageButtonParams.width = 150;
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            imageButtonParams.setMargins(0, 0, 0, 0);
            imageButtonParams2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            imageButtonParams2.width = 150;
            imageButtonParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            imageButtonParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            imageButtonParams2.setMargins(0, 0, 0, 0);

        } else if (T.getTipo() == 3) {
            imageButton.setBackgroundColor(Color.parseColor("#714933"));
            imageButton.setOnClickListener(view -> {
                contenedorTarea3.removeView(cardView);
                T.eliminarTarea(helperSql,T.getId());
                User.consultarUsuario(helperSql);
                User.obtenerRecompensa(10,"monedas",helperSql);
                User.obtenerRecompensa(1,"dados",helperSql);
                monedas.setText(String.valueOf(User.getMonedas()));
                dados.setText(String.valueOf(User.getDados()));
            });
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageButtonParams.width = 300;
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            imageButtonParams.setMargins(0, 0, 0, 0);

        } else if (T.getTipo() == 4) {
            imageButton.setBackgroundColor(Color.parseColor("#714933"));
            imageButton.setOnClickListener(view -> {
                contenedorTarea4.removeView(cardView);
                T.eliminarTarea(helperSql,T.getId());
                User.consultarUsuario(helperSql);
                User.obtenerRecompensa(10,"monedas",helperSql);
                User.obtenerRecompensa(1,"dados",helperSql);
                monedas.setText(String.valueOf(User.getMonedas()));
                dados.setText(String.valueOf(User.getDados()));
            });
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageButtonParams.width = 300;
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            imageButtonParams.setMargins(0, 0, 0, 0);
        } else {
            Log.e("error","Error al dar Recompensa");
        }

        TextView tituloTarea = new TextView(Menu.this);
        TextView descripcionTarea = new TextView(Menu.this);
        TextView fechaTarea = new TextView(Menu.this);
        if (T.getTipo() == 2){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(160, 0, 0, 0);
            tituloTarea.setLayoutParams(params);
            descripcionTarea.setLayoutParams(params);
            fechaTarea.setLayoutParams(params);
        }
        tituloTarea.setText(T.getTitulo());
        descripcionTarea.setText(T.getDescripcion());
        fechaTarea.setText(T.getFecha());
        tituloTarea.setTextColor(Color.parseColor("#FFFFFF"));
        descripcionTarea.setTextColor(Color.parseColor("#FFFFFF"));
        fechaTarea.setTextColor(Color.parseColor("#FFFFFF"));
        tituloTarea.setTextSize(20);
        descripcionTarea.setTextSize(20);
        fechaTarea.setTextSize(20);

        LinearLayout linearLayout = new LinearLayout(Menu.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.setMargins(16, 16, 16, 16);
        linearLayout.setLayoutParams(linearLayoutParams);

        linearLayout.addView(tituloTarea);
        linearLayout.addView(descripcionTarea);
        linearLayout.addView(fechaTarea);

        relativeLayout.addView(linearLayout);
        if (T.getTipo() == 2){
            relativeLayout.addView(imageButton2, imageButtonParams2);
        }
        relativeLayout.addView(imageButton, imageButtonParams);
        cardView.addView(relativeLayout);

        if (T.getTipo() == 1){
            contenedorTarea1.addView(cardView);
        } else if (T.getTipo() == 2){
            contenedorTarea2.addView(cardView);
        } else if (T.getTipo() == 3) {
            contenedorTarea3.addView(cardView);
        } else if (T.getTipo() == 4) {
            contenedorTarea4.addView(cardView);
        } else {
            Log.e("Error", "La tarea no tiene tipo: "+ T.getTipo());
        }

    }

    public void seleccionarTipo(){
        tipoGrupo.setOnCheckedChangeListener((group, checkedId) -> {
            float densidad = getResources().getDisplayMetrics().density;
            if (checkedId==R.id.tipo1){
                tipoTarea = 1;
                tipoScroll.smoothScrollTo(0,0);
            } else if (checkedId==R.id.tipo2) {
                tipoTarea = 2;
                tipoScroll.smoothScrollTo((int) (390 * densidad),0);
            } else if (checkedId==R.id.tipo3) {
                tipoTarea = 3;
                tipoScroll.smoothScrollTo((int) (780 * densidad),0);
            } else if (checkedId==R.id.tipo4) {
                tipoTarea = 4;
                tipoScroll.smoothScrollTo(4000,0);
            }
        });
    }

    private void consultarTareas(){

        SQLiteDatabase db = helperSql.getWritableDatabase();

        Cursor C = db.rawQuery("SELECT id, titulo, descripcion, tipo, fecha, dificultad FROM tareas",null);

        if (C.moveToFirst()){

            do {
                Integer id = C.getInt(0);
                String titulo = C.getString(1);
                String descripcion = C.getString(2);
                Integer tipo = C.getInt(3);
                String fecha = C.getString(4);
                Integer dificultad = C.getInt(5);

                Tarea T = new Tarea(id,titulo,descripcion,tipo,fecha,dificultad);

                crearTarjeta(T);
            } while (C.moveToNext());

        }

        C.close();
        db.close();
    }

    public void IntentTienda(View view) {
        Intent intent = new Intent(Menu.this, Tienda.class);
        startActivity(intent);
    }
    public void IntentCrearTarea(View view) {
        Intent intent = new Intent(Menu.this, CrearTarea.class);
        crearTarjetaLauncher.launch(intent);
    }
    public void IntentAventura(View view) {
        Intent intent = new Intent(Menu.this, Aventura.class);
        startActivity(intent);
    }

    public void IntentEditarTarea(Tarea T) {
        Intent intent = new Intent(Menu.this, EditarTarea.class);
        intent.putExtra("id", T.getId());
        startActivity(intent);
    }

}