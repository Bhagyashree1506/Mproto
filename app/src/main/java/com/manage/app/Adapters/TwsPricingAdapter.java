package com.manage.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manage.app.Models.ModelPrice;
import com.manage.app.R;
import com.manage.app.databinding.CardPriceBinding;

import java.util.ArrayList;

public class TwsPricingAdapter extends RecyclerView.Adapter<TwsPricingAdapter.MyViewHolder> {
    ArrayList<ModelPrice> mlist;
    Context context;

    public TwsPricingAdapter(ArrayList<ModelPrice> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TwsPricingAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_price,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelPrice price = mlist.get(position);


        holder.binding.type.setText(price.getType());
        holder.binding.value.setText(price.getPrice());




    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardPriceBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardPriceBinding.bind(itemView);

        }
    }
}
