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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Adapters.BookingListAdapter;
import com.manage.app.Models.ModelBooking;
import com.manage.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceAssignedActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingListAdapter adapter;
    private ArrayList<ModelBooking> modelBookingList;
    DatabaseReference DB_Cust,BoH;
    ProgressBar progressBar;
    SwipeRefreshLayout mySwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_assigned);
        mySwipeRefreshLayout=findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.pro_list_tb);

        getWindow().setStatusBarColor(ContextCompat.getColor(ServiceAssignedActivity.this,R.color.black));


        modelBookingList = new ArrayList<>();
        adapter = new BookingListAdapter(modelBookingList,ServiceAssignedActivity.this);

        recyclerView=findViewById(R.id.rv_todayBooking);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ServiceAssignedActivity.this));

        recyclerView.setAdapter(adapter);


        DB_Cust = FirebaseDatabase.getInstance().getReference("Users");
        BoH = FirebaseDatabase.getInstance().getReference("Bookings_on_hold");



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
        modelBookingList.clear();
        adapter.notifyDataSetChanged();
        BoH.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot Total_bookings_onHold) {
                if (Total_bookings_onHold.getChildrenCount()==0) {
                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.emp).setVisibility(View.VISIBLE);
                }else{
                    for(DataSnapshot Booking : Total_bookings_onHold.getChildren()) {



                        BoH.child(Booking.getKey()).orderByChild("status").equalTo("Assigned").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    DB_Cust.child(Booking.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot customer) {

                                            ModelBooking modelBooking = customer.getValue(ModelBooking.class);
                                            modelBookingList.add(modelBooking);



                                            adapter.notifyDataSetChanged();





                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });










                    }
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}