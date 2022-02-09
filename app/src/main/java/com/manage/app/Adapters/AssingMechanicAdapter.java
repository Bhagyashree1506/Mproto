package com.manage.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manage.app.Models.ModelMechanic;
import com.manage.app.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AssingMechanicAdapter extends RecyclerView.Adapter<AssingMechanicAdapter.MyViewHolder> {

    ArrayList<ModelMechanic> mlist;
    Context context;
    String phone,serviceID,mechanic;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }


    public AssingMechanicAdapter(ArrayList<ModelMechanic> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }


    @NotNull
    @Override
    public AssingMechanicAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_assign_mechanic,parent,false);

        return new AssingMechanicAdapter.MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NotNull AssingMechanicAdapter.MyViewHolder holder, int position) {
        ModelMechanic m = mlist.get(position);
        holder.name.setText(m.getFirstName()+" "+m.getLastName());
        holder.mechanicID.setText(m.getMechanicID());
        holder.phone.setText(m.getPhone());

        holder.mechStatus.setText(m.getMechStatus());


        DatabaseReference Cref = FirebaseDatabase.getInstance().getReference("Users").child(phone);
        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Cref.child("services").child(serviceID).child("mechanicID").setValue(m.getMechanicID());
                Cref.child("services").child(serviceID).child("serviceStatus").setValue("Assigned").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {

                    if(task.isSuccessful()){
                        DatabaseReference Mref = FirebaseDatabase.getInstance().getReference("mechanics").child(m.getPhone());
                        Mref.child("mechStatus").setValue("On Service");

                        HashMap<String,String> sv = new HashMap<>();

                        sv.put("phone",phone);
                        sv.put("Service_ID",serviceID);

                        Mref.child("Service_List").child(serviceID).setValue(sv);

                        DatabaseReference BoH = FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone).child(serviceID);

                        BoH.child("mechanic").setValue(m.getMechanicID());
                        BoH.child("status").setValue("Assigned");


                       // context.startActivity(new Intent(context,BookingDetailActivity.class).putExtra("phone",phone));
                        ((Activity) context).onBackPressed();

                    }

                }
            });



            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone,mechanicID,mechStatus;
        Button assign;
        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            mechStatus = itemView.findViewById(R.id.assignCard_Status);
            assign = itemView.findViewById(R.id.mech_assign_btn);
            name = itemView.findViewById(R.id.mech_name);
            phone = itemView.findViewById(R.id.mech_phone);

            mechanicID = itemView.findViewById(R.id.mech_id);

        }
    }
}
