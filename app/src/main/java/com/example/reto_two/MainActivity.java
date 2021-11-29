package com.example.reto_two;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_atrapa_pokemon,et_buscar_pokemon;
    private Button btn_atrapar_pokemon,btn_buscar_pokemon;

    private RecyclerView recycler_pokemones;
    private LinearLayoutManager manager;
    private AdaptadorPokemon adaptadorPokemon;
    private AdaptadorPokemon adaptadorPokemonSearch;

    private FirebaseFirestore dataBase;
    private ProgressDialog capturandoProgressBar;

    private String username;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_atrapa_pokemon = (EditText) findViewById(R.id.et_atrapa_pokemon);
        et_buscar_pokemon = (EditText) findViewById(R.id.et_buscar_pokemon);
        btn_atrapar_pokemon = (Button) findViewById(R.id.btn_atrapar_pokemon);
        btn_buscar_pokemon = (Button) findViewById(R.id.btn_buscar_pokemon);
        recycler_pokemones = (RecyclerView) findViewById(R.id.recycler_pokemones);
        capturandoProgressBar = new ProgressDialog(this);

        dataBase =FirebaseFirestore.getInstance();

        btn_atrapar_pokemon.setOnClickListener(this);
        btn_buscar_pokemon.setOnClickListener(this);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::OnResult//Aquí vemos como colocar una función como parámetro a partir de java 8
        );
        Bundle usuarioExiste = getIntent().getExtras();
        if(usuarioExiste!=null){
            int existe= usuarioExiste.getInt("Existe");
            this.username = usuarioExiste.getString("Username");
            adaptarDatosFirebase(existe,username);
        }else {
            finish();
        }
        configurateRecycler();

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
                                System.currentTimeMillis()+"",
                                pokemonApi.getId(),
                                pokemonApi.getName(),
                                pokemonApi.getAbilities()[0].getAbility().getName(),
                                pokemonApi.getStats()[2].getBase_stat(),
                                pokemonApi.getStats()[5].getBase_stat(),
                                pokemonApi.getStats()[0].getBase_stat(),
                                pokemonApi.getStats()[1].getBase_stat()
                        );

                        runOnUiThread(()->{
                            recycler_pokemones.setAdapter(adaptadorPokemon);
                        });
                        agregarPokemonAFirebase(pokemon);




                    }catch (Exception e){

                        runOnUiThread(
                                ()->{
                                    Toast.makeText(this,"¡Ha ocurrido un error!: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        );
                    }
                }
        ).start();


    }

    private void agregarPokemonAFirebase(Pokemon pokemon){
        dataBase.collection("Usuarios").document(username).collection("Pokemones").document(pokemon.getId()).set(pokemon).addOnCompleteListener(task -> {
            runOnUiThread( ()->{
                Toast.makeText(this,pokemon.getName()+" se ha subido a Firebase",Toast.LENGTH_SHORT).show();
            });
            adaptadorPokemon.addPokemon(pokemon);
        }).addOnFailureListener(task -> {
            runOnUiThread( ()->{
                Toast.makeText(this,"No se ha subido a Firebase",Toast.LENGTH_SHORT).show();
            });
        });


    }

    private void obtenerPokemonesDeFirebase(){
        dataBase.collection("Usuarios").document(username).collection("Pokemones").get().addOnCompleteListener(
          task -> {

              for(DocumentSnapshot snapshot: task.getResult()){
                  Pokemon pokemon = snapshot.toObject(Pokemon.class);
                  adaptadorPokemon.addPokemon(pokemon);
              }
          }
        );

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
                obtenerPokemonesDeFirebase();

                break;

            case EntrenadorPokemon.USUARIO_NO_EXISTE:
                welcomeMessage(username);
                EntrenadorPokemon e = new EntrenadorPokemon(username);
                dataBase.collection("Usuarios").document(username).set(e);
                break;
        }
    }


    private void configurateRecycler() {

        manager = new LinearLayoutManager(getApplicationContext());
        recycler_pokemones.setLayoutManager(manager);

        adaptadorPokemon= new AdaptadorPokemon(this);
        adaptadorPokemonSearch= new AdaptadorPokemon(this);
        recycler_pokemones.setAdapter(adaptadorPokemon);

        adaptadorPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = recycler_pokemones.getChildAdapterPosition(v);
                Pokemon pokemon = adaptadorPokemon.getPokemons().get(position);

                Bundle bundle= new Bundle();
                bundle.putSerializable("Pokemon",pokemon);
                bundle.putString("Username",username);

                Intent openProfilePokemon=new Intent(getApplicationContext(), PokemonActivity.class);
                openProfilePokemon.putExtras(bundle);
                launcher.launch(openProfilePokemon);

            }
        });

        adaptadorPokemonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = recycler_pokemones.getChildAdapterPosition(v);
                Pokemon pokemon = adaptadorPokemonSearch.getPokemons().get(position);

                Bundle bundle= new Bundle();
                bundle.putSerializable("Pokemon",pokemon);
                bundle.putString("Username",username);

                Intent openProfilePokemon=new Intent(getApplicationContext(), PokemonActivity.class);
                openProfilePokemon.putExtras(bundle);
                launcher.launch(openProfilePokemon);

            }
        });


    }


    private void welcomeMessage(String username){
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
                capturandoProgressBar.setMessage("Buscando Pokémon...");
                capturandoProgressBar.show();
                String searchPokemon = et_buscar_pokemon.getText().toString();
                if(!searchPokemon.equals("")){
                    searchPokemon(searchPokemon);
                }else {
                    Toast.makeText(getApplicationContext(),"Por favor ingresa un nombre válido",Toast.LENGTH_SHORT).show();
                }
                capturandoProgressBar.dismiss();
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

    private void searchPokemon(String searchPokemon) {
        adaptadorPokemonSearch.getPokemons().clear();
        try{
            int number= Integer.parseInt(searchPokemon);
            Query query = dataBase.collection("Usuarios").document(username).collection("Pokemones").whereEqualTo("idNumber",number+"");
            query.get().addOnCompleteListener(
                    task -> {
                        Pokemon pokemon= null;
                        if(task.getResult().size()!=0){
                            for(DocumentSnapshot doc : task.getResult()){
                                pokemon = doc.toObject(Pokemon.class);
                                Toast.makeText(this,"¡"+pokemon.getName()+" ha sido encontrado!",Toast.LENGTH_LONG).show();
                                Toast.makeText(this,"Para volver a ver tus Pokémones atrapa a otro Pokémon.",Toast.LENGTH_LONG).show();

                                adaptadorPokemonSearch.addPokemon(pokemon);
                                recycler_pokemones.setAdapter(adaptadorPokemonSearch);
                                et_buscar_pokemon.setText("");

                                break;
                            }
                        }

                    }
            ).addOnFailureListener(task->{
                Toast.makeText(this," No se ha encontrado el pokémon.",Toast.LENGTH_LONG).show();

            });



        }catch (NumberFormatException e){

            Query query = dataBase.collection("Usuarios").document(username).collection("Pokemones").whereEqualTo("name",searchPokemon);
            query.get().addOnCompleteListener(
                    task -> {
                        Pokemon pokemon= null;
                        if(task.getResult().size()!=0){
                            for(DocumentSnapshot doc : task.getResult()){
                                pokemon = doc.toObject(Pokemon.class);
                                Toast.makeText(this,"¡"+pokemon.getName()+" ha sido encontrado!",Toast.LENGTH_LONG).show();
                                Toast.makeText(this,"Para volver a ver tus Pokémones atrapa a otro Pokémon.",Toast.LENGTH_LONG).show();

                                adaptadorPokemonSearch.addPokemon(pokemon);
                                recycler_pokemones.setAdapter(adaptadorPokemonSearch);
                                et_buscar_pokemon.setText("");
                                break;
                            }
                        }
                    }
            ).addOnFailureListener(task->{
                Toast.makeText(this," No se ha encontrado el pokémon.",Toast.LENGTH_LONG).show();
            });
        }

        new Thread( ()->{

            try{
                Thread.sleep(1000);
                runOnUiThread(()->{
                    if(recycler_pokemones.getAdapter().equals(adaptadorPokemon)){
                        Toast.makeText(this,"No se ha encontrado el pokémon",Toast.LENGTH_SHORT).show();
                        et_buscar_pokemon.setText("");
                    }
                });
            }catch (Exception e){

            }

        }).start();



    }

    public void OnResult(ActivityResult result){

        if(result.getResultCode() == RESULT_OK){
            String pokemonId = result.getData().getExtras().getString("PokemonId");
            adaptadorPokemon.removePokemon(pokemonId);
            adaptadorPokemonSearch.removePokemon(pokemonId);

        }
    }

}