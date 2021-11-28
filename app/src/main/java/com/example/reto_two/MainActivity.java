package com.example.reto_two;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto_two.model.EntrenadorPokemon;
import com.example.reto_two.model.Pokemon;
import com.example.reto_two.pokeapi.PokemonApi;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_atrapa_pokemon,et_buscar_pokemon;
    private Button btn_atrapar_pokemon,btn_buscar_pokemon;
    private RecyclerView recycler_pokemones;
    private AdaptadorPokemon adaptadorPublicacion;
    private ArrayList<Pokemon> pokemonArrayList;
    private RelativeLayout relative_cagando;
    private FirebaseFirestore dataBase;
    private ProgressDialog capturandoProgressBar;

    private String username;
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
        capturandoProgressBar = new ProgressDialog(this);

        pokemonArrayList= new ArrayList<>();
        dataBase =FirebaseFirestore.getInstance();
        recycler_pokemones.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btn_atrapar_pokemon.setOnClickListener(this);
        btn_buscar_pokemon.setOnClickListener(this);

        Bundle usuarioExiste = getIntent().getExtras();
        if(usuarioExiste!=null){
            int existe= usuarioExiste.getInt("Existe");
            this.username = usuarioExiste.getString("Username");
            adaptarDatosFirebase(existe,username);
        }else {
            finish();
        }
    }

    //    urlFoto,String idPokemon , String nombre, int valorDefensa, int valorVelocidad, int valorVida, int valorAtaque) {
    private void atraparPokemon(String pokemonName){
        et_atrapa_pokemon.setText("");

        new Thread(
                ()->{
                    try{
                        HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
                        String json = utilDomi.GETrequest(pokemonName);
                        Gson gson = new Gson();
                        PokemonApi pokemonApi = gson.fromJson(json,PokemonApi.class);

                        Pokemon pokemon= new Pokemon(
                                pokemonApi.getSprites().getFront_default(),
                                UUID.randomUUID().toString(),
                                pokemonApi.getName(),
                                pokemonApi.getAbilities()[0].getAbility().getName(),
                                pokemonApi.getStats()[2].getBase_stat(),
                                pokemonApi.getStats()[5].getBase_stat(),
                                pokemonApi.getStats()[0].getBase_stat(),
                                pokemonApi.getStats()[1].getBase_stat()
                        );

                        pokemonArrayList.add(pokemon);
                        adaptadorPublicacion.setPokemons(pokemonArrayList);
                        agregarPokemonAFirebase(pokemon);

                    }catch (Exception e){

                        runOnUiThread(
                                ()->{
                                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        );
                    }
                }
        ).start();


    }

    private void agregarPokemonAFirebase(Pokemon pokemon){
        dataBase.collection("Usuarios").document(username).collection("Pokemones").document(pokemon.getId()).set(pokemon);
    }

    private void obtenerPokemonesDeFirebase(){
        dataBase.collection("Usuarios").document(username).collection("Pokemones").get().addOnCompleteListener(
          task -> {
              for(DocumentSnapshot snapshot: task.getResult()){
                  Pokemon pokemon = snapshot.toObject(Pokemon.class);
                  this.pokemonArrayList.add(pokemon);
              }
          }
        );
        llenarRecycler();
    }

    /**
     * Este método se encarga de obtener conectarse con firebase:
     * 1) Si el bundle dice que el usuario no existe: Muestra un mensaje de bienvenida y agrega a firebase firestore el nombre de usuario ingresado.
     * 2) "  "     "    "    "  "    "     si existe: Obtiene los datos de los pokemones atrapados y llena el recycler.
     * @param existe : El codigo del bundle sobre si existe el usuario e la db
     * @param username : El username ingresado en la activity de login
     */
    private void adaptarDatosFirebase(int existe, String username){

        switch (existe){
            case EntrenadorPokemon.USUARIO_EXISTE:
                relative_cagando.setVisibility(View.VISIBLE);
                obtenerPokemonesDeFirebase();

                break;

            case EntrenadorPokemon.USUARIO_NO_EXISTE:

                mensajeBienvenida(username);

                EntrenadorPokemon e = new EntrenadorPokemon(username);
                dataBase.collection("Usuarios").document(username).set(e);
                llenarRecycler();
                break;
        }

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


    private void llenarRecycler() {

        adaptadorPublicacion=new AdaptadorPokemon(this.pokemonArrayList,getApplicationContext(),MainActivity.this);

        adaptadorPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pokemon pokemon = pokemonArrayList.get(recycler_pokemones.getChildAdapterPosition(v));

                Bundle bundle= new Bundle();
                bundle.putSerializable("Pokemon",pokemon);

                Intent abrirPerfilPokemon=new Intent(getApplicationContext(), PokemonActivity.class);
                abrirPerfilPokemon.putExtras(bundle);
                startActivity(abrirPerfilPokemon);

            }
        });
        recycler_pokemones.setAdapter(adaptadorPublicacion);


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

                capturandoProgressBar.setMessage("Atrapando Pokémon...");
                capturandoProgressBar.show();
                String pokemonAtrapar = et_atrapa_pokemon.getText().toString();
                if(!pokemonAtrapar.equals("")){
                    atraparPokemon(pokemonAtrapar);
                }else {
                    Toast.makeText(getApplicationContext(),"Por favor ingresa un nombre válido",Toast.LENGTH_SHORT).show();
                }
                capturandoProgressBar.dismiss();
                break;

        }
    }


}