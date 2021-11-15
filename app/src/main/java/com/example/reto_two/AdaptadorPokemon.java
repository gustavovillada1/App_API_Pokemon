package com.example.reto_two;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reto_two.model.Pokemon;

import java.util.ArrayList;

public class AdaptadorPokemon extends RecyclerView.Adapter<AdaptadorPokemon.ViewHolderPokemon> implements View.OnClickListener{
    ArrayList<Pokemon> pokemons;
    private  View.OnClickListener listener;
    private Context c;

    public AdaptadorPokemon(ArrayList<Pokemon> pokemons, Context c) {
        this.pokemons = pokemons;
        this.c=c;
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

        holder.tv_recycler_nombre.setText(pokemons.get(position).getNombre());
        Glide
                .with(c)
                .load(pokemons.get(position).getUrlFoto())
                .into(holder.img_recycler_foto);


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


        public ViewHolderPokemon(@NonNull View itemView) {

            super(itemView);
            img_recycler_foto=(ImageView) itemView.findViewById(R.id.img_recycler_foto);
            tv_recycler_nombre=(TextView) itemView.findViewById(R.id.tv_recycler_nombre);



        }
    }
}
