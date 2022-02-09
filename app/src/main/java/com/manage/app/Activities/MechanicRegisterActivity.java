package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Models.Mechanic;
import com.manage.app.R;

import org.jetbrains.annotations.NotNull;

public class MechanicRegisterActivity extends AppCompatActivity {

    TextInputLayout firstNameL,lastNameL,phoneL,addressL,emailL,passwordL;
    DatabaseReference DBr;
    Button addMechanicRegister;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(MechanicRegisterActivity.this,R.color.black));
        dialog = new ProgressDialog(MechanicRegisterActivity.this);
        dialog.setMessage("Loading Servers...");
        dialog.setCancelable(false);

        /*------------------------------Hooks start---------------------------------------*/
        firstNameL = findViewById(R.id.FirstNameMechanicRegister);
        lastNameL = findViewById(R.id.LastNameMechanicRegister);
        phoneL = findViewById(R.id.phoneMechanicRegister);
        emailL = findViewById(R.id.emailMechanicRegister);
        addressL = findViewById(R.id.addressMechanicRegister);
        addMechanicRegister = findViewById(R.id.addMechanicRegister);
        passwordL = findViewById(R.id.passwordMechanicRegisterL);



        /*------------------------------Hooks end---------------------------------------*/


        /*------------------------------Variables---------------------------------------*/

            DBr= FirebaseDatabase.getInstance().getReference("mechanics");



        /*------------------------------Variables end---------------------------------------*/






        addMechanicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerMechanic();

            }
        });













    }


    public void registerMechanic(){
        dialog.show();
        if(firstNameL.getEditText().getText().toString().equals("") || lastNameL.getEditText().getText().toString().equals("") || emailL.getEditText().getText().toString().equals("") || addressL.getEditText().getText().toString().equals("") || phoneL.getEditText().getText().toString().equals("") || passwordL.getEditText().getText().toString().equals("")){
            Toast.makeText(MechanicRegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }





        String firstName = firstNameL.getEditText().getText().toString();
        String lastname = lastNameL.getEditText().getText().toString();
        String phone = phoneL.getEditText().getText().toString();
        String email = emailL.getEditText().getText().toString();
        String address = addressL.getEditText().getText().toString();
        String password = passwordL.getEditText().getText().toString();

        Query checkMechanic = DBr.orderByChild("phone").equalTo(phone);

        checkMechanic.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MechanicRegisterActivity.this);
                    builder.setMessage("Mechanic Already Exists");
                    builder.setIcon(R.drawable.ic_mechanic);
                    builder.show();


                }else{
                    dialog.dismiss();


                    String mechanicID = phone;
                    Mechanic mechanic = new Mechanic(firstName,lastname,phone,email,address,mechanicID,"Available",password,"0");

                    DBr.child(phone).setValue(mechanic).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(MechanicRegisterActivity.this, "Mechanic added successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MechanicRegisterActivity.this,MechanicDisplayActivity.class)
                                        .putExtra("phone",phone)
                                );
                                finish();
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