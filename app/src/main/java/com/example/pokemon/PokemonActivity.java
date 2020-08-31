package com.example.pokemon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PokemonActivity extends AppCompatActivity {
    private static Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_info);

        TextView nameView = findViewById(R.id.name);
        nameView.setText(pokemon.getpName());

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageBitmap(pokemon.getpImage());

        TextView heightView = findViewById(R.id.height);
        heightView.setText(Integer.toString(pokemon.getpHeight()));

        TextView weightView = findViewById(R.id.weight);
        weightView.setText(Integer.toString(pokemon.getpWeight()));

        TextView attackView = findViewById(R.id.attack);
        attackView.setText(Integer.toString(pokemon.getpAttack()));

        TextView defenseView = findViewById(R.id.defense);
        defenseView.setText(Integer.toString(pokemon.getpDefense()));

        TextView hpView = findViewById(R.id.hp);
        hpView.setText(Integer.toString(pokemon.getpHP()));

        StringBuilder str = new StringBuilder();
        for (String s : pokemon.getpTypes()) {
            str.append(s).append(" ");
        }
        TextView typesView = findViewById(R.id.types);
        typesView.setText(str.toString());

    }

    public static void setPokemon(Pokemon p) {
        pokemon = p;
    }
}
