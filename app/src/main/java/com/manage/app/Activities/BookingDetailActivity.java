package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Models.ModelService;
import com.manage.app.R;
import com.manage.app.Adapters.ServiceMechanicAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BookingDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceMechanicAdapter adapter;
    private ArrayList<ModelService> modelServiceList;
    DatabaseReference DB_SerVice,DBref,BoH;
    TextInputLayout firstNameBD,lastNameBD,phoneBD,emailBD;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ProgressBar progressBar;

    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        getWindow().setStatusBarColor(ContextCompat.getColor(BookingDetailActivity.this,R.color.black));




        /*------------------------------Variables---------------------------------------*/
        String phone = getIntent().getStringExtra("phone");
        DB_SerVice = FirebaseDatabase.getInstance().getReference("Users").child(phone).child("services");
        DBref= FirebaseDatabase.getInstance().getReference("Users");

        firstNameBD = findViewById(R.id.firstNameBD);
        lastNameBD = findViewById(R.id.lastNameBD);
        phoneBD = findViewById(R.id.phoneBD);
        progressBar = findViewById(R.id.pro_list_bd);
        emailBD = findViewById(R.id.emailBD);

        mySwipeRefreshLayout=findViewById(R.id.swiperefresh);


        /*------------------------------Variables end---------------------------------------*/

        recyclerView=findViewById(R.id.rv_serviceDetail);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(BookingDetailActivity.this));



        modelServiceList = new ArrayList<>();
        adapter = new ServiceMechanicAdapter(modelServiceList,BookingDetailActivity.this);
        adapter.setPhone(phone);


        recyclerView.setAdapter(adapter);


        Query checkUser = DBref.orderByChild("phone").equalTo(phone);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    firstNameBD.getEditText().setText(snapshot.child(phone).child("firstName").getValue(String.class));
                    phoneBD.getEditText().setText(snapshot.child(phone).child("phone").getValue(String.class));
                    lastNameBD.getEditText().setText(snapshot.child(phone).child("lastName").getValue(String.class));
                    emailBD.getEditText().setText(snapshot.child(phone).child("email").getValue(String.class));

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        BoH = FirebaseDatabase.getInstance().getReference("Bookings_on_hold").child(phone);


        ArrayList<ModelService> insertList = new ArrayList<>();

        makeList();

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                       makeList();
                       mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


    }

    private void makeList() {
        progressBar.setVisibility(View.VISIBLE);

        modelServiceList.clear();


        BoH.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot Booking) {

                for(DataSnapshot Services : Booking.getChildren()){


                    DB_SerVice.child(Services.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot specific_Service) {


                            if(specific_Service.exists()){
                                ModelService modelService = specific_Service.getValue(ModelService.class);

                                modelServiceList.add(modelService);
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }




                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {



                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

}