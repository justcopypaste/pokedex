package com.example.users.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.users.PokemonActivity;
import com.example.users.R;
import com.example.users.models.Pokemon;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Pokemon> pokemons;

    public PokemonAdapter(Context context) {
        this.context = context;
        pokemons = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pokemon_card, viewGroup, false);

        return new ViewHolder(view);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Pokemon p = pokemons.get(position);
        String pId ="" + p.getId();
        viewHolder.getPokemonId().setText(pId);
        viewHolder.getPokemonName().setText(p.getName());
        Picasso.get().load(p.getImage()).into(viewHolder.getPokemonImage());
        viewHolder.getPokemonImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PokemonActivity.class);

                Bundle b = new Bundle();
                b.putInt("pokemonId", p.getId());
                intent.putExtras(b);

                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void clearPokemons() {
        pokemons = new ArrayList<>();
    }

    public void addPokemon(Pokemon p) {
        pokemons.add(p);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(pokemons, Comparator.comparing(Pokemon::getId));
        }
        notifyDataSetChanged();
    }

    public void addPokemons(ArrayList<Pokemon> p){
        pokemons.addAll(p);
        notifyDataSetChanged();
    }

    public ArrayList<Pokemon> getPokemons(){
        return pokemons;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView pokemonName;
        private final TextView pokemonId;
        private final ImageView pokemonImage;

        public ViewHolder(View view) {
            super(view);
            pokemonId = (TextView) view.findViewById(R.id.pokemonId);
            pokemonName = (TextView) view.findViewById(R.id.pokemonName);
            pokemonImage = (ImageView) view.findViewById(R.id.pokemonImage);
        }

        public TextView getPokemonId() {
            return pokemonId;
        }

        public TextView getPokemonName() {
            return pokemonName;
        }

        public ImageView getPokemonImage() {
            return pokemonImage;
        }
    }



}
