package com.example.reto_two;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private RelativeLayout relative_cagando;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    boolean cargandoThread=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_atrapa_pokemon = (EditText) findViewById(R.id.et_atrapa_pokemon);
        et_buscar_pokemon = (EditText) findViewById(R.id.et_buscar_pokemon);
        btn_atrapar_pokemon = (Button) findViewById(R.id.btn_atrapar_pokemon);
        btn_buscar_pokemon = (Button) findViewById(R.id.btn_buscar_pokemon);
        recycler_pokemones = (RecyclerView) findViewById(R.id.recycler_pokemones);
        relative_cagando = (RelativeLayout) findViewById(R.id.relative_cagando);

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
                relative_cagando.setVisibility(View.VISIBLE);
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

                mensajeBienvenida(username);

                ArrayList<Pokemon> pkms = new ArrayList();
                pkms.add(new Pokemon("http://assets.stickpng.com/images/580b57fcd9996e24bc43c325.png",150,"Pikachu",100,120,200,123));
                EntrenadorPokemon e = new EntrenadorPokemon(username,pkms);
                db.collection("Usuarios").document(username).set(e);
                break;
        }

    }


    private void llenarPublicaciones(ArrayList<Pokemon> pokemons) {



        AdaptadorPokemon adaptadorPublicacion=new AdaptadorPokemon(pokemons,getApplicationContext(),MainActivity.this);

        adaptadorPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pokemon pokemon = pokemons.get(recycler_pokemones.getChildAdapterPosition(v));

                Bundle bundle= new Bundle();
                bundle.putSerializable("Pokemon",pokemon);

                Intent abrirPerfilPokemon=new Intent(getApplicationContext(), PokemonActivity.class);
                abrirPerfilPokemon.putExtras(bundle);
                startActivity(abrirPerfilPokemon);

            }
        });
        recycler_pokemones.setAdapter(adaptadorPublicacion);

        new Thread(
                ()-> {
                    while (cargandoThread) {
                        try {
                            Thread.sleep(1000);
                            runOnUiThread(
                                    () -> {
                                        relative_cagando.setVisibility(View.GONE);
                                        cargandoThread=false;
                                    }
                            );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }


    private void mensajeBienvenida(String username){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popup_bienvenida =  getLayoutInflater().inflate(R.layout.popup_bienvenida, null);

        TextView tv_popup_bienvenida = (TextView) popup_bienvenida.findViewById(R.id.tv_popup_bienvenida);
        tv_popup_bienvenida.setText("¡Hola "+username+", te damos la bienvenida a Pokémon POKEDEX! \n Esperamos que disfrutes el juego, y ¡atrapa a todos los Pokemons!");
        dialogBuilder.setView(popup_bienvenida);
        dialog = dialogBuilder.create();
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_buscar_pokemon:

                break;

            case R.id.btn_atrapar_pokemon:

                String pokemonAtrapar = et_atrapa_pokemon.getText().toString();
                if(!pokemonAtrapar.equals("")){
                    atraparPokemonHTTP(pokemonAtrapar);
                }else {
                    Toast.makeText(getApplicationContext(),"Por favor ingresa un nombre válido",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void atraparPokemonHTTP(String pokemonAtrapar) {
        new Thread(
                ()->{
                    HTTPSWebUtilDomi h = new HTTPSWebUtilDomi();
                    h.GETrequest("https://geogameicesi.herokuapp.com/api/geo/set");
                }
        ).start();
    }
}