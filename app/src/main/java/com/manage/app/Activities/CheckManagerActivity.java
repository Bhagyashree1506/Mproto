package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.manage.app.R;
import com.manage.app.databinding.ActivityCheckManagerBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckManagerActivity extends AppCompatActivity {
    ActivityCheckManagerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getWindow().setStatusBarColor(ContextCompat.getColor(CheckManagerActivity.this, R.color.black));



        binding.Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.codeField.setError(null);
                String accessCode = binding.codeField.getEditText().getText().toString();
                if(accessCode.length()>0){

                    FirebaseDatabase.getInstance().getReference("Admin").child("AccessCode").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){
                                String acDB = snapshot.getValue(String.class);

                                if(accessCode.equals(acDB)){
                                    startActivity(new Intent(CheckManagerActivity.this,RegisterActivity.class));
                                }else{
                                    binding.codeField.setError("Incorrect Access Code");
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    binding.codeField.setError("Invalid AccessCode");
                }


            }
        });


    }
}