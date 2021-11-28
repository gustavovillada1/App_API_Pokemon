package com.example.reto_two;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reto_two.model.Pokemon;

import java.util.ArrayList;

public class AdaptadorPokemon extends RecyclerView.Adapter<AdaptadorPokemon.ViewHolderPokemon> implements View.OnClickListener{
    private ArrayList<Pokemon> pokemons;
    private  View.OnClickListener listener;
    private Context c;
    private Activity activity;

    public AdaptadorPokemon(ArrayList<Pokemon> pokemons, Context c, Activity activity) {
        this.pokemons = pokemons;
        this.c=c;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolderPokemon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_recycler,parent,false);

        view.setOnClickListener(this);
        return new ViewHolderPokemon(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPokemon holder, int position) {

        holder.tv_recycler_nombre.setText(pokemons.get(position).getName());
        Glide
                .with(c)
                .load(pokemons.get(position).getSprites())
                .into(holder.img_recycler_foto);

        new Thread( ()->{
            while(true){
                try {
                    Thread.sleep(1000);
                    activity.runOnUiThread(
                            ()->{
                                holder.progressBar3.setVisibility(View.GONE);
                            }
                    );

                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    @Override
    public int getItemCount() {

        return pokemons.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    @Override
    public void onClick(View v) {

        if(listener!=null){

            listener.onClick(v);
        }

    }


    public class ViewHolderPokemon extends RecyclerView.ViewHolder{

        TextView tv_recycler_nombre;
        ImageView img_recycler_foto;
        ProgressBar progressBar3;


        public ViewHolderPokemon(@NonNull View itemView) {

            super(itemView);
            progressBar3 = (ProgressBar) itemView.findViewById(R.id.progressBar3);
            img_recycler_foto=(ImageView) itemView.findViewById(R.id.img_recycler_foto);
            tv_recycler_nombre=(TextView) itemView.findViewById(R.id.tv_recycler_nombre);



        }
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
}
