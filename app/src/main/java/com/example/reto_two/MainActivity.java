package com.example.reto_two;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.reto_two.model.EntrenadorPokemon;
import com.example.reto_two.model.Pokemon;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_atrapa_pokemon,et_buscar_pokemon;
    private Button btn_atrapar_pokemon,btn_buscar_pokemon;
    private RecyclerView recycler_pokemones;
    private ArrayList<Pokemon> pokemonArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_atrapa_pokemon = (EditText) findViewById(R.id.et_atrapa_pokemon);
        et_buscar_pokemon = (EditText) findViewById(R.id.et_buscar_pokemon);
        btn_atrapar_pokemon = (Button) findViewById(R.id.btn_atrapar_pokemon);
        btn_buscar_pokemon = (Button) findViewById(R.id.btn_buscar_pokemon);
        recycler_pokemones = (RecyclerView) findViewById(R.id.recycler_pokemones);

        pokemonArrayList= new ArrayList<>();
        recycler_pokemones.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btn_atrapar_pokemon.setOnClickListener(this);
        btn_buscar_pokemon.setOnClickListener(this);

        Bundle usuarioExiste = getIntent().getExtras();
        if(usuarioExiste!=null){
            int existe= usuarioExiste.getInt("Existe");
            String username = usuarioExiste.getString("Username");
            adaptarDatosFirebase(existe,username);
        }else {
            finish();
        }
    }

    /**
     * Este método se encarga de obtener conectarse con firebase:
     * 1) Si el bundle dice que el usuario no existe: Muestra un mensaje de bienvenida y agrega a firebase firestore el nombre de usuario ingresado.
     * 2) "  "     "    "    "  "    "     si existe: Obtiene los datos de los pokemones atrapados y llena el recycler.
     * @param existe : El codigo del bundle sobre si existe el usuario e la db
     * @param username : El username ingresado en la activity de login
     */
    private void adaptarDatosFirebase(int existe, String username){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        switch (existe){
            case EntrenadorPokemon.USUARIO_EXISTE:
                db.collection("Usuarios").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            EntrenadorPokemon e = documentSnapshot.toObject(EntrenadorPokemon.class);

                            llenarPublicaciones(e.getPokemons());
                        }

                    }
                });

                break;

            case EntrenadorPokemon.USUARIO_NO_EXISTE:

                ArrayList<Pokemon> pkms = new ArrayList();
                pkms.add(new Pokemon("http://assets.stickpng.com/images/580b57fcd9996e24bc43c325.png",150,"Pikachu",100,120,200,123));
                EntrenadorPokemon e = new EntrenadorPokemon(username,pkms);
                db.collection("Usuarios").document(username).set(e);
                break;
        }

    }


    private void llenarPublicaciones(ArrayList<Pokemon> pokemons) {

        AdaptadorPokemon adaptadorPublicacion=new AdaptadorPokemon(pokemons,getApplicationContext());

        adaptadorPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        recycler_pokemones.setAdapter(adaptadorPublicacion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_buscar_pokemon:

                break;

            case R.id.btn_atrapar_pokemon:

                break;

        }
    }
}