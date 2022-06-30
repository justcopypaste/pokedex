package com.example.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.users.adapters.PokemonAdapter;
import com.example.users.models.Pokemon;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "POKEDEX";

    private RecyclerView recyclerView;
    private PokemonAdapter pokemonAdapter;
    RequestQueue requestQueue;

    private int cantPokemon = 16;
    ArrayList<Pokemon> pokemons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        pokemonAdapter = new PokemonAdapter(this);
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.setHasFixedSize(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        Spinner spinner = (Spinner) findViewById(R.id.cantPokemon);
        spinner.setOnItemSelectedListener(this);

    }

    void getData(){
        final String url_base = "https://pokeapi.co/api/v2/pokemon/";

        pokemonAdapter.clearPokemons();

        for (int i = 1; i < cantPokemon; i++) {
            String url = url_base + i;

            JsonObjectRequest pokemonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Pokemon p = new Pokemon();
                            p.setName(response.optString("name"));
                            p.setId(response.optInt("id"));

                            JSONObject sprites = response.isNull("sprites") ? null : response.optJSONObject("sprites");
                            String sprite = sprites.optString("front_default");
                            p.setImage(sprite);

                            pokemonAdapter.addPokemon(p);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Log.i(TAG,"Error: " + error);
                        }
                    });

            requestQueue.add(pokemonRequest);
        }


    }

    public void setCantPokemon(int cant){
        cantPokemon = cant+1;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String input = parent.getItemAtPosition(pos).toString();
        int cant = Integer.parseInt(input);
        setCantPokemon(cant);
        getData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}