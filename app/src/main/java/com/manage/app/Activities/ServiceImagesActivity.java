package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.manage.app.R;
import com.manage.app.Adapters.ServiceImageAdapter;
import com.manage.app.Models.SingleImage;
import com.manage.app.databinding.ActivityServiceImagesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceImagesActivity extends AppCompatActivity {
    ActivityServiceImagesBinding binding;
    DatabaseReference imgRef;
    ArrayList<String> links;
    ServiceImageAdapter adapter;
    String phone,serviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ServiceImagesActivity.this, R.color.yelight));



        links = new ArrayList<>();
        adapter = new ServiceImageAdapter(links,ServiceImagesActivity.this);
        phone = getIntent().getStringExtra("phone");
        serviceID = getIntent().getStringExtra("serviceID");

        imgRef = FirebaseDatabase.getInstance().getReference("Users").child(phone)
                .child("services").child(serviceID).child("images");

        binding.imageList.setLayoutManager(new LinearLayoutManager(ServiceImagesActivity.this));
        binding.imageList.setHasFixedSize(true);
        binding.imageList.setAdapter(adapter);


        makeList();


    }

    private void makeList() {
        links.clear();
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot images) {

                if(images.exists()){

                    for(DataSnapshot singleImage : images.getChildren() ){
                            SingleImage single =  singleImage.getValue(SingleImage.class);

                            links.add(single.getImageUrl());
                        adapter.notifyDataSetChanged();

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}