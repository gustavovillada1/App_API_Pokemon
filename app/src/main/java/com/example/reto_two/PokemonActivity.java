package com.example.reto_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reto_two.model.Pokemon;
import com.google.firebase.firestore.FirebaseFirestore;

public class PokemonActivity extends AppCompatActivity {

    private Pokemon pokemon;
    private String username;
    private int index;
    private Button btn_soltar;
    private ImageView img_foto_perfil_pokemon;
    private TextView tv_perfil_nombre_pokemon,tv_perfil_defensa,tv_perfil_ataque,tv_perfil_velocidad,tv_perfil_vida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);


        Bundle pokemonEnviado = getIntent().getExtras();
        pokemon=null;
        if(pokemonEnviado!=null){
            pokemon=(Pokemon) pokemonEnviado.getSerializable("Pokemon");
            username= pokemonEnviado.getString("Username","");
        }else {
            finish();
        }

        btn_soltar = (Button) findViewById(R.id.btn_soltar);
        img_foto_perfil_pokemon = (ImageView) findViewById(R.id.img_foto_perfil_pokemon);
        tv_perfil_nombre_pokemon = (TextView) findViewById(R.id.tv_perfil_nombre_pokemon);
        tv_perfil_defensa = (TextView) findViewById(R.id.tv_perfil_defensa);
        tv_perfil_ataque = (TextView) findViewById(R.id.tv_perfil_ataque);
        tv_perfil_velocidad = (TextView) findViewById(R.id.tv_perfil_velocidad);
        tv_perfil_vida = (TextView) findViewById(R.id.tv_perfil_vida);

        llenarDatos();

        btn_soltar.setOnClickListener(this::dropPokemon);

    }

    private void dropPokemon(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Usuarios").document(username).collection("Pokemones").document(pokemon.getId()).delete().addOnCompleteListener(task ->{
            Toast.makeText(this,"??"+pokemon.getName()+" ha sido soltado!",Toast.LENGTH_SHORT).show();
            Intent data = new Intent();

            data.putExtra("PokemonId",pokemon.getId());

            setResult(RESULT_OK, data);
            finish();
        } ).addOnFailureListener(task->{
            Toast.makeText(this,"??Ha ocurrido un error!",Toast.LENGTH_SHORT).show();
            Intent data = new Intent();

            setResult(RESULT_CANCELED, data);
            finish();
        });

    }

    private void llenarDatos(){
        Glide.with(getApplicationContext()).load(pokemon.getSprites()).into(img_foto_perfil_pokemon);
        tv_perfil_nombre_pokemon.setText(pokemon.getName()+"\n("+pokemon.getAbility()+")");
        tv_perfil_defensa.setText(pokemon.getDefense()+"");
        tv_perfil_ataque.setText(pokemon.getAttack()+"");
        tv_perfil_velocidad.setText(pokemon.getSpeed()+"");
        tv_perfil_vida.setText(pokemon.getHp()+"");
    }
}