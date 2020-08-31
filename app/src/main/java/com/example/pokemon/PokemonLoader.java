package com.example.pokemon;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class PokemonLoader extends AsyncTaskLoader<List<Pokemon>> {
    private String mUrl;

    public PokemonLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Pokemon> loadInBackground() {
        if (mUrl == null) return null;

        return QueryUtils.extractPokemons(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
