package com.manage.app.Adapters;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.manage.app.Activities.GenerateImageActivity;
import com.manage.app.Activities.ListAssignMechanicActivity;
import com.manage.app.Activities.ServiceApprovalActivity;
import com.manage.app.Activities.ServiceImagesActivity;
import com.manage.app.Models.ModelService;
import com.manage.app.R;
import com.manage.app.databinding.AssignedServiceEditBinding;
import com.manage.app.databinding.DeleteBookingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceMechanicAdapter extends RecyclerView.Adapter<ServiceMechanicAdapter.MyViewHolder>{

    ArrayList<ModelService> mlist;
    Context context;
    String phone;
    String mecnName;
    String vhNo;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ServiceMechanicAdapter(ArrayList<ModelService> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }







    @NotNull
    @Override
    public ServiceMechanicAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_service_mechanic,parent,false);

        return new ServiceMechanicAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ServiceMechanicAdapter.MyViewHolder holder, int position) {
        ModelService m = mlist.get(position);
        holder.date.setText(m.getDate());
        holder.time.setText(m.getTime());
        holder.vehicleNo.setText(vhNo);
        holder.ServiceStatus.setText(m.getStatus());
        holder.address.setText(m.getLocation().getTxt());



        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("LatLng", m.getLocation().getLat()+","+m.getLocation().getLng());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "LatLng copied to clip board", Toast.LENGTH_SHORT).show();


            }
        });





        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View vi = LayoutInflater.from(context).inflate(R.layout.delete_booking,null);
                DeleteBookingBinding binding = DeleteBookingBinding.bind(vi);
                AlertDialog builder = new AlertDialog.Builder(context).create();
                builder.setIcon(R.drawable.ic_mechanic);
                builder.setTitle("Manage | GearToCare");
                builder.setView(binding.getRoot());

                builder.show();



                binding.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("Users").child(phone)
                                .child("services").child(m.getServiceID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mlist.remove(position);
                                    notifyDataSetChanged();


                                    if(m.getStatus().equals("Done") || m.getStatus().equals("Assigned")){

                                        FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone)
                                                .child(m.getServiceID()).setValue(null);

                                        FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).
                                                child("Service_List").child(m.getServiceID()).setValue(null);
                                    }


                                    builder.dismiss();
                                }
                            }
                        });


                    }
                });


                return false;
            }
        });



        FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone).child(m.getServiceID())
                .child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).equals("Done")){
                    holder.assignMechanic.setVisibility(View.GONE);
                    holder.images.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);
                    holder.generatePdf.setVisibility(View.VISIBLE);



                    holder.generatePdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(context, GenerateImageActivity.class);

                            i.putExtra("phone", m.getPhone());
                            i.putExtra("serviceID", m.getServiceID());


                            context.startActivity(i);

                        }
                    });



                    holder.approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();



                            FirebaseDatabase.getInstance().getReference("Users").child(phone).child("services")
                                    .child(m.getServiceID()).child("serviceStatus").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){


                                        FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID())
                                                .child("service_counter").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    Integer service_counter = Integer.parseInt(snapshot.getValue(String.class));

                                                    service_counter = service_counter+Integer.valueOf(1);
                                                    FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID())
                                                            .child("service_counter").setValue(String.valueOf(service_counter)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                mlist.remove(position);
                                                                notifyDataSetChanged();

                                                                FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone).child(m.getServiceID())
                                                                        .setValue(null);

                                                                FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).child("Service_List").child(m.getServiceID())
                                                                        .setValue(null);

                                                                progressDialog.dismiss();

                                                                Intent i = new Intent(context, ServiceApprovalActivity.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                context.startActivity(i);
                                                                Toast.makeText(context, "Service marked as completed", Toast.LENGTH_LONG).show();





                                                            }

                                                        }
                                                    });

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });





                                    }


                                }
                            });



                        }
                    });


                    holder.images.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ServiceImagesActivity.class);

                            intent.putExtra("phone",phone);
                            intent.putExtra("serviceID",m.getServiceID());

                            context.startActivity(intent);
                        }
                    });

                }else{

                    holder.assignMechanic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(m.getStatus().equals("On_Hold")){
                                Intent intent = new Intent(context, ListAssignMechanicActivity.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("serviceID",m.getServiceID());
                                intent.putExtra("mechanicID",m.getMechanicID());
                                context.startActivity(intent);
                            }else{

                                FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        View vi = LayoutInflater.from(context).inflate(R.layout.assigned_service_edit,null);
                                        AssignedServiceEditBinding binding = AssignedServiceEditBinding.bind(vi);
                                        AlertDialog builder = new AlertDialog.Builder(context).create();
                                        builder.setIcon(R.drawable.ic_mechanic);
                                        builder.setTitle("Manage | GearToCare");
                                        builder.setView(binding.getRoot());


                                        binding.mechName.setText(snapshot.child("firstName").getValue(String.class)+" "+snapshot.child("lastName").getValue(String.class));

                                        binding.appCompatButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                FirebaseDatabase.getInstance().getReference("mechanics").child(m.getMechanicID()).child("Service_List").child(m.getServiceID()).setValue(null);
                                                FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone).child(m.getServiceID()).child("status").setValue("On_Hold");

                                                FirebaseDatabase.getInstance().getReference("Users").child(phone).child("services").child(m.getServiceID()).child("serviceStatus")
                                                        .setValue("On_Hold").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        m.setStatus("On_Hold");
                                                        holder.ServiceStatus.setText("On_Hold");
                                                        builder.dismiss();

                                                    }
                                                });





                                            }
                                        });



                                        builder.show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });


                            }


                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }





    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date,time,vehicleNo,address,ServiceStatus;
        Button assignMechanic,images,approve,generatePdf;


        public MyViewHolder(@NotNull View itemView) {
            super(itemView);

            //Hooks
            ServiceStatus = itemView.findViewById(R.id.serviceCard_Status);
            date = itemView.findViewById(R.id.serviceCard_date);
            time= itemView.findViewById(R.id.serviceCard_time);
            vehicleNo = itemView.findViewById(R.id.serviceCard_vehicleNo);
            address = itemView.findViewById(R.id.serviceCard_Address);
            assignMechanic = itemView.findViewById(R.id.cardMechanic_assign_btn);
            images=itemView.findViewById(R.id.cardMechanic_seeImages_btn);
            approve = itemView.findViewById(R.id.cardMechanic_approve_btn);
            generatePdf = itemView.findViewById(R.id.cardMechanic_gen_pdf);
        }
    }




}
