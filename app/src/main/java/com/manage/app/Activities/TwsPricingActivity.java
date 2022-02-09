package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Adapters.TwsCompanyAdapter;
import com.manage.app.Adapters.TwsPricingAdapter;
import com.manage.app.Helpers.CustomProgressDialog;
import com.manage.app.Models.ModelPrice;
import com.manage.app.R;
import com.manage.app.databinding.ActivityTwsPricingBinding;

import java.util.ArrayList;

public class TwsPricingActivity extends AppCompatActivity {
    ActivityTwsPricingBinding binding;
    ArrayList<ModelPrice> priceList;
    TwsPricingAdapter adapter;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwsPricingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(TwsPricingActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(TwsPricingActivity.this);
        priceList = new ArrayList<>();
        adapter = new TwsPricingAdapter(priceList,TwsPricingActivity.this);
        DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("Pricing");
        binding.rvTwsPricing.setAdapter(adapter);
        binding.rvTwsPricing.setLayoutManager(new LinearLayoutManager(TwsPricingActivity.this));
        binding.rvTwsPricing.setHasFixedSize(true);


        makeList();


    }

    private void makeList() {
        progressDialog.show();
        priceList.clear();
        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot check_snap) {

                if (check_snap.exists()) {

                    for (DataSnapshot checkItem : check_snap.getChildren()) {

                        priceList.add(checkItem.getValue(ModelPrice.class));

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