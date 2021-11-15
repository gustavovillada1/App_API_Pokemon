package com.example.reto_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reto_two.model.EntrenadorPokemon;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {


    private EditText et_username;
    private Button btn_ingresar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username);
        btn_ingresar = (Button) findViewById(R.id.btn_ingresar);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = et_username.getText().toString();
                if(!username.equals("")){
                    iniciarSesion(username);
                }else {
                    Toast.makeText(getApplicationContext(),"Ingresa un nombre válido",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * Método encargado de iniciar sesion, verifica si el usuario existe o no en la base de datos firestore.
     */
    private void iniciarSesion(String username) {

        db.collection("Usuarios").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Bundle bundle = new Bundle();

                if(documentSnapshot.exists()){
                    bundle.putInt("Existe", EntrenadorPokemon.USUARIO_EXISTE);

                }else {
                    bundle.putInt("Existe", EntrenadorPokemon.USUARIO_NO_EXISTE);

                }
                bundle.putString("Username", username);
                Intent iniciar=new Intent(LoginActivity.this, MainActivity.class);
                iniciar.putExtras(bundle);
                startActivity(iniciar);

            }
        });
    }

}