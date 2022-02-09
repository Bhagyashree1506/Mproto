package com.manage.app.Activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.manage.app.Adapters.PlacesAutoCompleteAdapter;
import com.manage.app.R;
import com.manage.app.databinding.ActivitySelectLocationBinding;
import com.google.android.libraries.places.api.Places;

public class SelectLocationActivity extends AppCompatActivity  {
    private static final String TAG = "12";
    ActivitySelectLocationBinding binding;
    int AUTOCOMPLETE_REQUEST_CODE = 111;
    PlacesAutoCompleteAdapter adapter;

    private Handler mHandler;
    String[] hints={"Pravin Hardware","Poonam Chamber","Eternity Mall"};
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(SelectLocationActivity.this, R.color.black));

        //Changing location hint
        i=0;
        binding.searchHint.setMovementMethod(new ScrollingMovementMethod());
        Runnable mUpdate = new Runnable() {
            public void run() {

                binding.searchHint.setText(hints[i]);
                i++;
                mHandler.postDelayed(this, 3000);

                if(i>=hints.length){
                    i=0;
                }
            }
        };

        mHandler = new Handler();
        mHandler.post(mUpdate);




        binding.clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.search.setText("");
            }
        });


        Places.initialize(SelectLocationActivity.this,"AIzaSyAQtEk43RaJ2Jp-6EnqK2qXz58OuJZKZCE");

        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(SelectLocationActivity.this));
        binding.placesRecyclerView.setHasFixedSize(true);
        adapter = new PlacesAutoCompleteAdapter(SelectLocationActivity.this);


        binding.placesRecyclerView.setAdapter(adapter);


        adapter.notifyDataSetChanged();
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){

                    binding.clearSearch.setVisibility(View.VISIBLE);
                    //binding.or.setVisibility(View.GONE);
                    binding.placesRecyclerView.setVisibility(View.VISIBLE);
                    binding.art.setVisibility(View.GONE);
                    binding.or.setVisibility(View.GONE);
                    adapter.getFilter().filter(s.toString());
                }else{
                    binding.clearSearch.setVisibility(View.GONE);
                    //binding.or.setVisibility(View.VISIBLE);
                    binding.placesRecyclerView.setVisibility(View.GONE);
                    binding.art.setVisibility(View.VISIBLE);
                }
            }
        });


    }




}