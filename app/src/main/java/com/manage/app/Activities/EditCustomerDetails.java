package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.manage.app.R;
import com.manage.app.databinding.ActivityEditCustomerDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditCustomerDetails extends AppCompatActivity {
    ActivityEditCustomerDetailsBinding binding;
    DatabaseReference DBref;
    String phone;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCustomerDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(EditCustomerDetails.this, R.color.yelight));

        phone = getIntent().getStringExtra("phone");
        dialog = new ProgressDialog(EditCustomerDetails.this);
        dialog.setMessage("Please wait...");
        dialog.setIcon(R.drawable.ic_mechanic);

        DBref = FirebaseDatabase.getInstance().getReference("Users").child(phone);

        showCustomerDetails();


        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                HashMap<String,Object> m = new HashMap<>();

                m.put("firstName",binding.FirstNameRegister.getText().toString());
                m.put("lastName",binding.LastNameRegister.getText().toString());
                m.put("phone",binding.phoneRegister.getText().toString());
                m.put("email",binding.emailRegsiter.getText().toString());

                DBref.updateChildren(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Intent i = new Intent(EditCustomerDetails.this, CustomerDisplayActivity.class);
                            i.putExtra("phone",phone);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        }


                    }
                });

            }
        });




    }

    private void showCustomerDetails() {

        DBref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.FirstNameRegister.setText(snapshot.child("firstName").getValue(String.class));
                binding.LastNameRegister.setText(snapshot.child("lastName").getValue(String.class));
                binding.emailRegsiter.setText(snapshot.child("email").getValue(String.class));
                binding.phoneRegister.setText(snapshot.child("phone").getValue(String.class));




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}