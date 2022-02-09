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
import com.manage.app.Helpers.CustomProgressDialog;
import com.manage.app.R;
import com.manage.app.databinding.ActivityTwsCompanyBinding;

import java.util.ArrayList;

public class TwsCompanyActivity extends AppCompatActivity {
    ActivityTwsCompanyBinding binding;
    ArrayList<String> companyList;
    TwsCompanyAdapter adapter;
    DatabaseReference DBref;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwsCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(TwsCompanyActivity.this, R.color.black));
        progressDialog = new CustomProgressDialog(TwsCompanyActivity.this);
        companyList = new ArrayList<>();
        adapter = new TwsCompanyAdapter(companyList, TwsCompanyActivity.this);

        DBref = FirebaseDatabase.getInstance().getReference("Services").child("TwoWheelerService").child("CompanyList");
        binding.rvTwsCompany.setAdapter(adapter);
        binding.rvTwsCompany.setLayoutManager(new LinearLayoutManager(TwsCompanyActivity.this));
        binding.rvTwsCompany.setHasFixedSize(true);


        makeList();


    }


    private void makeList() {
        progressDialog.show();
        companyList.clear();
        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot check_snap) {

                if (check_snap.exists()) {

                    for (DataSnapshot checkItem : check_snap.getChildren()) {

                        companyList.add(checkItem.getValue(String.class));

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