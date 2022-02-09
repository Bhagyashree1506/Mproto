package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Adapters.ManageCouponAdapter;
import com.manage.app.Adapters.ManageSlotAdapter;
import com.manage.app.Helpers.CustomProgressDialog;
import com.manage.app.Models.ModelCoupon;
import com.manage.app.R;
import com.manage.app.databinding.ActivityManageSlotBinding;
import com.manage.app.databinding.AddCouponBinding;
import com.manage.app.databinding.AddSlotBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ManageSlotActivity extends AppCompatActivity {
    ActivityManageSlotBinding binding;
    ManageSlotAdapter adapter;
    ArrayList<String> slots;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageSlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManageSlotActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(ManageSlotActivity.this);

        slots = new ArrayList<>();
        adapter = new ManageSlotAdapter(slots, ManageSlotActivity.this);


        binding.rvSlots.setLayoutManager(new LinearLayoutManager(ManageSlotActivity.this));
        binding.rvSlots.setHasFixedSize(true);
        binding.rvSlots.setAdapter(adapter);


        makeList();

        AddSlotBinding asb = AddSlotBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(ManageSlotActivity.this);
        dialog.setContentView(asb.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);


        asb.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot = asb.slotTime.getText().toString();

                if (slot.isEmpty()) {
                    Toast.makeText(ManageSlotActivity.this, "Please enter valid slot", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                progressDialog.show();
                slots.add(slot);

                Integer[] slotArray = new Integer[slots.size()];

                for (int i = 0; i < slots.size(); i++) {
                    slotArray[i] = Integer.valueOf(slots.get(i));
                }

                Arrays.sort(slotArray);

                HashMap<String, Object> slot_map = new HashMap<>();
                for (int i = 0; i < slots.size(); i++) {
                    slot_map.put(String.valueOf(i + 1), String.valueOf(slotArray[i]));
                }

                FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").updateChildren(slot_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            makeList();


                        }


                    }
                });


            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();


            }
        });


    }

    public void makeList() {
        progressDialog.show();
        slots.clear();


        FirebaseDatabase.getInstance().getReference("AppManager").child("SlotManager").child("timeSlots").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot timeSlots) {

                if (timeSlots.exists()) {


                    for (DataSnapshot timeSlot : timeSlots.getChildren()) {

                        slots.add(timeSlot.getValue(String.class));


                    }
                    adapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}