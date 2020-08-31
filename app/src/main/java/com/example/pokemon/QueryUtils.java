package com.example.pokemon;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static ArrayList<Pokemon> extractPokemons(String strUrl) {

        URL url = createURL(strUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Pokemon> pokemons = new ArrayList<>();

        JSONObject jsonPokemons = null;
        try {
            jsonPokemons = new JSONObject(jsonResponse);
            JSONArray arrayPokemons = jsonPokemons.getJSONArray("results");
            for (int i = 0; i < arrayPokemons.length(); i++) {

                ArrayList<String> types = new ArrayList<>();

                JSONObject currentObject = arrayPokemons.getJSONObject(i);
                String urlCurrentPokemon = currentObject.getString("url");
                URL urlPokemon = createURL(urlCurrentPokemon);
                String jsonResponsePokemon = null;
                try {
                    jsonResponsePokemon = makeHttpRequest(urlPokemon);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing input stream", e);
                }

                JSONObject jsonPoke = null;
                jsonPoke = new JSONObject(jsonResponsePokemon);

                String name = jsonPoke.getString("name");

                int height = jsonPoke.getInt("height");

                int weight = jsonPoke.getInt("weight");

                JSONArray arrayTypes = jsonPoke.getJSONArray("types");
                for (int j = 0; j < arrayTypes.length(); j++) {
                    JSONObject currentType = arrayTypes.getJSONObject(j);
                    JSONObject objectType = currentType.getJSONObject("type");
                    String type = objectType.getString("name");
                    types.add(type);
                }

                JSONArray arrayStats = jsonPoke.getJSONArray("stats");
                JSONObject currentStat = arrayStats.getJSONObject(0);
                int hp = currentStat.getInt("base_stat");
                currentStat = arrayStats.getJSONObject(1);
                int attack = currentStat.getInt("base_stat");
                currentStat = arrayStats.getJSONObject(2);
                int defense = currentStat.getInt("base_stat");

                JSONObject currentSprites = jsonPoke.getJSONObject("sprites");
                String imageUrl = currentSprites.getString("front_default");
                Bitmap image = doIn(imageUrl);

                pokemons.add(new Pokemon(name, image, height, weight, types, hp, attack, defense));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return pokemons;
    }

    public static URL createURL(String strurl){
        URL url = null;
        try {
            url = new URL(strurl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //я думаю что в основном здесь подгрузка тормозит
    private static Bitmap doIn(String urls) {
        String urldisplay = urls;
        Bitmap mIcon11 = null;
        try {

            InputStream in = new java.net.URL(urldisplay).openStream();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка передачи изображения");
            e.printStackTrace();
        }
        return mIcon11;
    }

}

