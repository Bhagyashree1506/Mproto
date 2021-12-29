package com.manage.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CustomerDisplayActivity extends AppCompatActivity {
    TextInputLayout firstNameD,lastNameD,phoneD,emailD;
    DatabaseReference DBref;
    Button newBooking,pastBooking,edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_display);
        getWindow().setStatusBarColor(ContextCompat.getColor(CustomerDisplayActivity.this,R.color.yelight));



        /*------------------------------Hooks start---------------------------------------*/
        firstNameD = findViewById(R.id.FirstNameCustomerDisplay);
        lastNameD = findViewById(R.id.LastNameCustomerDisplay);
        emailD = findViewById(R.id.emailCustomerDisplay);
        phoneD = findViewById(R.id.phoneCustomerDisplay);
        newBooking = findViewById(R.id.newBooking);
        pastBooking = findViewById(R.id.pastBooking);
        edit = findViewById(R.id.edit);
        /*addCustomeD = findViewById(R.id.addCustomerRegister);*/



        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/
        String phone = getIntent().getStringExtra("phone");

        /*------------------------------Variables end---------------------------------------*/


        DBref= FirebaseDatabase.getInstance().getReference("customers");

        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    firstNameD.getEditText().setText(snapshot.child(phone).child("firstName").getValue(String.class));
                    phoneD.getEditText().setText(snapshot.child(phone).child("phone").getValue(String.class));
                    lastNameD.getEditText().setText(snapshot.child(phone).child("lastName").getValue(String.class));
                    emailD.getEditText().setText(snapshot.child(phone).child("email").getValue(String.class));






                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerDisplayActivity.this,EditCustomerDetails.class)
                .putExtra("phone",phone)
                );
            }
        });


        newBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CustomerDisplayActivity.this,CustomerServiceActivity.class).putExtra("phone",phone));

            }
        });


        pastBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerDisplayActivity.this,ListServiceActivity.class).putExtra("phone",phone));
            }
        });







    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(CustomerDisplayActivity.this,CustomerActvity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

        );
        finish();
    }
}