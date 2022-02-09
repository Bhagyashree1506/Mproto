package com.manage.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manage.app.R;
import com.manage.app.SessionManager;

import org.jetbrains.annotations.NotNull;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SessionManager sessionManager;
    TextView profile_full_name,profile_user_key;
    TextInputLayout profile_phone,profile_email,profile_FullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getWindow().setStatusBarColor(ContextCompat.getColor(UserProfileActivity.this,R.color.black));
        /*update = findViewById(R.id.update_user);*/


        /*------------------------------Hooks start---------------------------------------*/
        profile_full_name = findViewById(R.id.profile_full_name);
        profile_user_key = findViewById(R.id.profile_user_key);
        profile_FullName = findViewById(R.id.profile_FullName);
        profile_email = findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);

        drawerLayout = findViewById(R.id.drawer_layout_user_profile);
        navigationView = findViewById(R.id.nav_view_user_profile);
        toolbar = findViewById(R.id.toolbar_user_profile);
        /*------------------------------Hooks end---------------------------------------*/



        /*------------------------------Variables---------------------------------------*/
        sessionManager = new SessionManager(UserProfileActivity.this);
        /*------------------------------Variables Ends---------------------------------------*/







        showAllUSerData();



        /*------------------------------Navigation Part Starts---------------------------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
       /* menu.findItem(R.id.nav_admin).setVisible(false);
        menu.findItem(R.id.nav_tutorial).setVisible(false);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UserProfileActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(UserProfileActivity.this);
        navigationView.setCheckedItem(R.id.nav_profile);
        navigationView.bringToFront();
        navigationView.requestLayout();
        /*------------------------------Navigation Part Ends---------------------------------------*/




    }

    public void showAllUSerData(){



        profile_full_name.setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));
        profile_email.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_EMAIL));
        profile_phone.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_PHONENUMBER));

        profile_FullName.getEditText().setText(sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_FIRSTNAME)+" "+sessionManager.getUsersDetailsFromSessions().get(sessionManager.KEY_lASTNAME));



    }








    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            startActivity(new Intent(UserProfileActivity.this, CustomerActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

            );
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_ninja:
                startActivity(new Intent(UserProfileActivity.this, CustomerActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                );
                finish();
                break;


                case R.id.nav_Mechanic:
                    startActivity(new Intent(UserProfileActivity.this,MechanicActivity.class));
                    finish();


                    break;

            case R.id.nav_Manage:
                startActivity(new Intent(UserProfileActivity.this,ManageActivity.class));
                finish();

                break;



            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                new SessionManager(UserProfileActivity.this).logoutSession();

                startActivity(new Intent(UserProfileActivity.this,LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );
                finish();

                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

}





    public void checkUpdatesbtn(){

        DatabaseReference UPref =  FirebaseDatabase.getInstance().getReference("AppStatus");
        Query checkUser = UPref.child("LatestVersion");
        SessionManager sessionManager = new SessionManager(UserProfileActivity.this);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Double latestVersion = snapshot.getValue(Double.class);
                Double currentVersion = Double.parseDouble(sessionManager.getUsersDetailsFromSessions().get(sessionManager.VERSION));
                if(latestVersion>currentVersion){


                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                    builder.setIcon(R.drawable.ic_ninja);
                    builder.setTitle("Ninja's | GearToCare");

                    builder.setMessage("Update Available \nVersion : " + latestVersion);

                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                    builder.setIcon(R.drawable.ic_ninja);
                    builder.setTitle("Ninja's | GearToCare");

                    builder.setMessage("You are using the latest version");

                    builder.show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }


}