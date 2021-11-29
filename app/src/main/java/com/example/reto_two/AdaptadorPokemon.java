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
import java.util.Collection;
import java.util.Collections;

public class AdaptadorPokemon extends RecyclerView.Adapter<AdaptadorPokemon.ViewHolderPokemon> implements View.OnClickListener{
    private ArrayList<Pokemon> pokemons;
    private  View.OnClickListener listener;
    private Context c;

    public AdaptadorPokemon( Context c) {
        this.pokemons = new ArrayList<>();
        this.c=c;
    }

    public void addPokemon(Pokemon p){
        this.pokemons.add(p);
        notifyItemInserted(pokemons.size());
    }


    public void removePokemon(String pokemonId){
        for(int i=0;i<this.pokemons.size();i++){
            if(this.pokemons.get(i).getId().equals(pokemonId)){
                this.pokemons.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    /**
     * Nos permite agregar el esqueleto del recyclerview
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolderPokemon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_recycler,parent,false);

        view.setOnClickListener(this);
        return new ViewHolderPokemon(view);
    }

    /**
     * Nos permite agregar información al esqueleto del recyclerview
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolderPokemon holder, int position) {
        Pokemon p = pokemons.get(position);

        holder.getTv_recycler_nombre().setText(p.getName());
        Glide.with(c).load(p.getSprites()).into(holder.getImg_recycler_foto());


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

        private TextView tv_recycler_nombre;
        private ImageView img_recycler_foto;
        private ProgressBar progressBar3;


        public ViewHolderPokemon(@NonNull View itemView) {

            super(itemView);
            img_recycler_foto=(ImageView) itemView.findViewById(R.id.img_recycler_foto);
            tv_recycler_nombre=(TextView) itemView.findViewById(R.id.tv_recycler_nombre);
        }

        public TextView getTv_recycler_nombre() {
            return tv_recycler_nombre;
        }

        public ImageView getImg_recycler_foto() {
            return img_recycler_foto;
        }

        public ProgressBar getProgressBar3() {
            return progressBar3;
        }

    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }


}
