package com.example.pokemon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PokeAdapter extends RecyclerView.Adapter<PokeAdapter.RecyclerViewHolder> {
    List<Pokemon> list;
    LayoutInflater layoutInflater;
    Context context;
    private ItemClickListener mClickListener;

    public PokeAdapter(Context context, List<Pokemon> list)
    {
        this.list=list;
        this.context=context;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Pokemon pokemon = getItem(position);
        holder.nameView.setText(pokemon.getpName());
        holder.imageView.setImageBitmap(pokemon.getpImage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Pokemon getItem(int position) {
        return list.get(position);
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView;
        ImageView imageView;

        public RecyclerViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.item_name);
            imageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public int addItem(final List<Pokemon> listt) {

        final int oldsize = list.size();
        for (int i = 0; i < listt.size()-1; i++){
            list.add(listt.get(i));
        }

        /*notifyItemRangeInserted(listt.size()-oldsize, oldsize);
        notifyItemChanged(listt.size());*/
        return oldsize;
    }

    public List<Pokemon> getList() {
        return list;
    }
}
