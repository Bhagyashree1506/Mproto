package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.Adapters.AssingMechanicAdapter;
import com.manage.app.Models.ModelMechanic;
import com.manage.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListAssignMechanicActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AssingMechanicAdapter adapter;
    private ArrayList<ModelMechanic> modelAssignMechanicList;
    DatabaseReference DBr ;
    SwipeRefreshLayout srl;
    String phone;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assign_mechanic);
        srl=findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.pro_list_am);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListAssignMechanicActivity.this,R.color.black));



        /*------------------------------Variables---------------------------------------*/
        phone = getIntent().getStringExtra("phone");
        String serviceID = getIntent().getStringExtra("serviceID");
        String mechanicID = getIntent().getStringExtra("mechanicID");

        DBr = FirebaseDatabase.getInstance().getReference("mechanics");

        /*------------------------------Variables end---------------------------------------*/

        recyclerView=findViewById(R.id.rv_assign_mechanic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListAssignMechanicActivity.this));


        modelAssignMechanicList = new ArrayList<>();
        adapter = new AssingMechanicAdapter(modelAssignMechanicList,ListAssignMechanicActivity.this);
        adapter.setPhone(phone);
        adapter.setServiceID(serviceID);

        recyclerView.setAdapter(adapter);



        makelist();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makelist();
                srl.setRefreshing(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(ListAssignMechanicActivity.this,BookingDetailActivity.class);
        i.putExtra("phone",phone);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
        finish();
    }

    private void makelist() {
        progressBar.setVisibility(View.VISIBLE);
        modelAssignMechanicList.clear();

        DBr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelMechanic modelCustomer =dataSnapshot.getValue(ModelMechanic.class);
                    modelAssignMechanicList.add(modelCustomer);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {


            }
        });
    }


}