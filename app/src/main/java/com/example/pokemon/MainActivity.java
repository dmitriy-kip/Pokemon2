package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Pokemon>>, PokeAdapter.ItemClickListener {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://pokeapi.co/api/v2/pokemon";;
    private TextView mEmptyStateTextView;
    private RecyclerView recyclerView;
    private PokeAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int limit = 30;
    private int offset = 0;
    private int numberLoader = 1;
    private int maxAttackIndex = 0;
    private int maxDefenseIndex = 0;
    private int maxHpIndex = 0;
    private int hpAttackIndex = 0;
    private int hpDefenseIndex = 0;
    private int attackDefenseIndex = 0;
    private int allIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PokeAdapter(this, new ArrayList<Pokemon>());

        mEmptyStateTextView = findViewById(R.id.empty_view);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(numberLoader, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Toast.makeText(MainActivity.this, "Uploading pokemons", Toast.LENGTH_SHORT).show();
                            offset += 30;
                            LoaderManager loaderManager = getLoaderManager();
                            loaderManager.restartLoader(numberLoader,null, MainActivity.this);
                            View loadingIndicator = findViewById(R.id.loading_indicator);
                            loadingIndicator.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        CheckBox checkAttack = findViewById(R.id.attack_check);
        CheckBox checkDefense = findViewById(R.id.defense_check);
        CheckBox checkHp = findViewById(R.id.hp_check);

        if (checkAttack.isChecked()) mAdapter.notifyItemMoved(maxAttackIndex,0);

        if (checkDefense.isChecked()) mAdapter.notifyItemMoved(maxDefenseIndex,0);

        if (checkHp.isChecked()) mAdapter.notifyItemMoved(maxHpIndex,0);

        if (checkAttack.isChecked() && checkDefense.isChecked()) mAdapter.notifyItemMoved(attackDefenseIndex,0);

        if (checkHp.isChecked() && checkAttack.isChecked()) mAdapter.notifyItemMoved(hpAttackIndex, 0);

        if (checkHp.isChecked() && checkDefense.isChecked()) mAdapter.notifyItemMoved(hpDefenseIndex, 0);

        if (checkHp.isChecked() && checkDefense.isChecked() && checkAttack.isChecked()) mAdapter.notifyItemMoved(allIndex, 0);

    }

    @Override
    public Loader<List<Pokemon>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("limit", Integer.toString(limit));
        uriBuilder.appendQueryParameter("offset", Integer.toString(offset));

        return new PokemonLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Pokemon>> loader, List<Pokemon> pokes) {
        View loadingIndicator = findViewById(R.id.loading_indicator);

        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_pokemons);

        if (pokes != null && !pokes.isEmpty()){
            int a = mAdapter.addItem(pokes);
            mAdapter.setClickListener(this);
            recyclerView.setAdapter(mAdapter);
            //я понимаю, что срочка ниже поный треш, но не как не могу добится что бы список не возврвщался наверх
            recyclerView.scrollToPosition(a-7);
            mEmptyStateTextView.setVisibility(View.GONE);
            loading = true;
            setMaxIndex();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Pokemon>> loader) {
        mAdapter = new PokeAdapter(this, new ArrayList<Pokemon>());
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, PokemonActivity.class);
        Pokemon pokemon = mAdapter.getItem(position);
        PokemonActivity.setPokemon(pokemon);
        startActivity(intent);
    }

    public void refresh(View view) {
        offset = (int) (Math.random() * 1018);
        mAdapter = new PokeAdapter(this, new ArrayList<Pokemon>());
        recyclerView.setAdapter(mAdapter);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.destroyLoader(numberLoader);
        loaderManager.initLoader(numberLoader,null, MainActivity.this);
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    public void setMaxIndex() {
        ArrayList<Pokemon> currentList = new ArrayList<>(mAdapter.getList());

        int maxAttack = 0;
        int maxDefense = 0;
        int maxHp = 0;
        int maxhpAttack = 0;
        int maxhpDefense = 0;
        int maxattackDefense = 0;
        int maxall = 0;


        for (int i = 0; i < currentList.size()-1; i++) {

            if (currentList.get(i).getpAttack() > maxAttack) {
                maxAttack = currentList.get(i).getpAttack();
                maxAttackIndex = i;
            }

            if (currentList.get(i).getpDefense() > maxDefense) {
                maxDefense = currentList.get(i).getpDefense();
                maxDefenseIndex = i;
            }

            if (currentList.get(i).getpHP() > maxHp) {
                maxHp = currentList.get(i).getpHP();
                maxHpIndex = i;
            }

            if (currentList.get(i).getpHP() + currentList.get(i).getpAttack() >  maxhpAttack){
                maxhpAttack = currentList.get(i).getpHP() + currentList.get(i).getpAttack();
                hpAttackIndex = i;
            }

            if (currentList.get(i).getpHP() + currentList.get(i).getpDefense() >  maxhpDefense){
                maxhpAttack = currentList.get(i).getpHP() + currentList.get(i).getpDefense();
                hpDefenseIndex = i;
            }

            if (currentList.get(i).getpAttack() + currentList.get(i).getpDefense() >  maxattackDefense){
                maxattackDefense = currentList.get(i).getpHP() + currentList.get(i).getpDefense();
                attackDefenseIndex = i;
            }

            if (currentList.get(i).getpAttack() + currentList.get(i).getpDefense() + currentList.get(i).getpHP() >  maxall){
                maxall = currentList.get(i).getpAttack() + currentList.get(i).getpDefense() + currentList.get(i).getpHP();
                allIndex = i;
            }
        }

    }

}