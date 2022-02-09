package com.manage.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manage.app.Activities.BookingDetailActivity;
import com.manage.app.Models.ModelBooking;
import com.manage.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.MyViewHolder> {

    ArrayList<ModelBooking> mlist;
    Context context;
    //Button viewDetails;

    public BookingListAdapter(ArrayList<ModelBooking> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }




    @NotNull
    @Override
    public BookingListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.booking_card,parent,false);
        return new BookingListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull BookingListAdapter.MyViewHolder holder, int position) {
        if(mlist.size()==0){
            ((Activity) context).findViewById(R.id.emp).setVisibility(View.VISIBLE);
        }


        ModelBooking m = mlist.get(position);
        holder.firstName.setText(m.getFirstName());
        holder.phone.setText(m.getPhone());




        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, BookingDetailActivity.class).putExtra("phone",holder.phone.getText().toString()));
            }
        });

    }

    public void updateList(ArrayList<ModelBooking> data) {
        mlist = data;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button viewDetails;

        TextView firstName,phone;
        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            firstName = itemView.findViewById(R.id.bookingCard_name);
            phone= itemView.findViewById(R.id.bookingCard_Phone);
            viewDetails = itemView.findViewById(R.id.bookingCard_ViewDetails);


        }





    }


}
