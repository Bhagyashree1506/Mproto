package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Adapters.ManageCouponAdapter;
import com.manage.app.Adapters.ManagePackageAdapter;
import com.manage.app.Models.ModelCoupon;
import com.manage.app.Models.ModelPackage;
import com.manage.app.R;
import com.manage.app.databinding.ActivityManagePackageBinding;

import java.util.ArrayList;

public class ManagePackageActivity extends AppCompatActivity {
    ActivityManagePackageBinding binding;
    ManagePackageAdapter adapter;
    ArrayList<ModelPackage> packages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagePackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(ManagePackageActivity.this, R.color.black));


        packages = new ArrayList<>();
        adapter = new ManagePackageAdapter(packages, ManagePackageActivity.this);

        binding.rvPackage.setHasFixedSize(true);
        binding.rvPackage.setAdapter(adapter);
        binding.rvPackage.setLayoutManager(new LinearLayoutManager(ManagePackageActivity.this));


        makeList();

    }

    private void makeList() {
        packages.clear();

        FirebaseDatabase.getInstance().getReference("AppManager").child("PackageManager").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot packageList) {

                if (packageList.exists()) {

                    for (DataSnapshot pack : packageList.getChildren()) {
                        ModelPackage m = pack.getValue(ModelPackage.class);


                        packages.add(m);


                    }
                    adapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}