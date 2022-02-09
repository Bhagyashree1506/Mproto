package com.manage.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manage.app.Models.ModelPackage;
import com.manage.app.R;
import com.manage.app.databinding.CardManagePackageBinding;
import com.manage.app.databinding.CardManageServiceBinding;

import java.util.ArrayList;

public class ManagePackageAdapter extends RecyclerView.Adapter<ManagePackageAdapter.MyViewHolder> {
    ArrayList<ModelPackage> mList;
    Context context;


    public ManagePackageAdapter(ArrayList<ModelPackage> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ManagePackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagePackageAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_manage_package,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePackageAdapter.MyViewHolder holder, int position) {
        ModelPackage mp = mList.get(position);

        holder.binding.packageName.setText(mp.getName());
        holder.binding.packageDescription.setText(mp.getDescription());
        holder.binding.packagePrice.setText(mp.getCost());
        holder.binding.packageServicePrice.setText(mp.getServiceCost());
        holder.binding.packageValidity.setText(mp.getValidity());
        holder.binding.packageVehicleCount.setText(mp.getVehicleCount());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CardManagePackageBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = CardManagePackageBinding.bind(itemView);
        }
    }
}
