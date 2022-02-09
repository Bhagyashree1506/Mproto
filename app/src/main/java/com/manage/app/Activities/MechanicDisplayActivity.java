package com.manage.app.Activities;

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
import com.manage.app.R;

import org.jetbrains.annotations.NotNull;

public class MechanicDisplayActivity extends AppCompatActivity {
    TextInputLayout firstNameD,lastNameD,phoneD,emailD,addressD;
    DatabaseReference DBref;
    Button newBooking,pastBooking;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_display);
        getWindow().setStatusBarColor(ContextCompat.getColor(MechanicDisplayActivity.this,R.color.black));




        /*------------------------------Hooks start---------------------------------------*/
        firstNameD = findViewById(R.id.FirstNameMechanicDisplay);
        lastNameD = findViewById(R.id.LastNameMechanicDisplay);
        emailD = findViewById(R.id.emailMechanicDisplay);
        phoneD = findViewById(R.id.phoneMechanicDisplay);
        addressD = findViewById(R.id.addressMechanicDisplay);
        edit = findViewById(R.id.edit);

        /*addCustomeD = findViewById(R.id.addCustomerRegister);*/



        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/
        String phone = getIntent().getStringExtra("phone");

        /*------------------------------Variables end---------------------------------------*/


        DBref= FirebaseDatabase.getInstance().getReference("mechanics");

        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    firstNameD.getEditText().setText(snapshot.child(phone).child("firstName").getValue(String.class));
                    phoneD.getEditText().setText(snapshot.child(phone).child("phone").getValue(String.class));
                    lastNameD.getEditText().setText(snapshot.child(phone).child("lastName").getValue(String.class));
                    emailD.getEditText().setText(snapshot.child(phone).child("email").getValue(String.class));
                    addressD.getEditText().setText(snapshot.child(phone).child("address").getValue(String.class));






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
                startActivity(new Intent(MechanicDisplayActivity.this,EditMechanicDetailActivity.class)
                        .putExtra("phone",phone)
                );
            }
        });


    }
}