package com.example.users;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends Activity {
    private static final String TAG = "POKEDEX";

    RequestQueue requestQueue;

    private ImageView pokemonImage;
    private TextView pokemonId;
    private TextView pokemonName;
    private TextView ability1;
    private TextView ability2;
    private TextView type1;
    private TextView type2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_page);

        pokemonName = (TextView) findViewById(R.id.pokemonName);
        pokemonId = (TextView) findViewById(R.id.pokemonId);
        ability1 = (TextView) findViewById(R.id.ability1);
        ability2 = (TextView) findViewById(R.id.ability2);
        type1 = (TextView) findViewById(R.id.type1);
        type2 = (TextView) findViewById(R.id.type2);
        pokemonImage = (ImageView) findViewById(R.id.pokemonImage);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        Bundle b = getIntent().getExtras();
        int pokemonId = b == null ? 0 : b.getInt("pokemonId");
        getData(this, pokemonId);


    }

    private void getData(Context context, int id){
        final String url_base = "https://pokeapi.co/api/v2/pokemon/";

            String url = url_base + id;

            JsonObjectRequest pokemonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String id = "#" + response.optInt("id");
                            pokemonId.setText(id);

                            String name = response.optString("name");
                            pokemonName.setText(name);

                            JSONObject sprites = response.isNull("sprites") ? null : response.optJSONObject("sprites");
                            String sprite = sprites.optString("front_default");
                            Picasso.get().load(sprite).into(pokemonImage);

                            JSONArray types = response.isNull("abilities") ? null : response.optJSONArray("types");

                            //type1
                            try {
                                JSONObject type1Json = ((JSONObject) types.get(0)).optJSONObject("type");
                                String type1Name = type1Json.optString("name");
                                type1.setText(type1Name);

                                int colorId = context.getResources().getIdentifier(type1Name, "color", context.getPackageName());
                                Drawable border = DrawableCompat.wrap(type1.getBackground());
                                DrawableCompat.setTint(border, ContextCompat.getColor(context, colorId));

//                                GradientDrawable drawable = (GradientDrawable)view.getBackground();
//                                drawable.mutate(); // only change this instance of the xml, not all components using this xml
//                                drawable.setStroke(3, Color.RED); // set stroke width and stroke color

                            } catch (JSONException e) {
                                type1.setVisibility(View.GONE);
                                e.printStackTrace();
                            }

                            //type2
                            try {
                                JSONObject type2Json = ((JSONObject) types.get(1)).optJSONObject("type");
                                String type2Name = type2Json.optString("name");
                                type2.setText(type2Name);

                                int colorId = context.getResources().getIdentifier(type2Name, "color", context.getPackageName());
                                Drawable border = DrawableCompat.wrap(type2.getBackground());
                                DrawableCompat.setTint(border, ContextCompat.getColor(context, colorId));

                            } catch (JSONException e) {
                                type2.setVisibility(View.GONE);
                                e.printStackTrace();
                            }

                            JSONArray abilities = response.isNull("abilities") ? null : response.optJSONArray("abilities");

                            //ability1
                            try {
                                JSONObject ability1Json = ((JSONObject) abilities.get(0)).optJSONObject("ability");
                                String ability1Name = ability1Json.optString("name");
                                ability1.setText(ability1Name);
                            } catch (JSONException e) {
                                ability1.setVisibility(View.GONE);
                                e.printStackTrace();
                            }

                            //ability2
                            try {
                                JSONObject ability2Json = ((JSONObject) abilities.get(1)).optJSONObject("ability");
                                String ability2Name = ability2Json.optString("name");
                                ability2.setText(ability2Name);
                            } catch (JSONException e) {
                                ability2.setVisibility(View.GONE);
                                e.printStackTrace();
                            }



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG,"Error: " + error);
                        }
                    });

            requestQueue.add(pokemonRequest);
        }

}


