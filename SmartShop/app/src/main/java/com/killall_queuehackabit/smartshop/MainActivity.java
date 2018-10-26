package com.killall_queuehackabit.smartshop;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.media.Image;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private TextView Date1,Date2,Date3,Date4,Date5,Amount1,Amount2,Amount3,Amount4,Amount5,navName,navEmail;
    private ImageView navImg;
    private LinearLayout orderList;
    private int past_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavigationView mNavigationView = (NavigationView) findViewById(R.id.my_navigation_view);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(MainActivity.this);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, scanner.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        myDrawer = (DrawerLayout)findViewById(R.id.MyDrawer);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        Date1 = (TextView)findViewById(R.id.Date1);
        Date2 = (TextView)findViewById(R.id.Date2);
        Date3 = (TextView)findViewById(R.id.Date3);
        Date4 = (TextView)findViewById(R.id.Date4);
        Date5 = (TextView)findViewById(R.id.Date5);
        navImg = (ImageView)findViewById(R.id.navImg);
        orderList = (LinearLayout)findViewById(R.id.orderList);
        Amount1 = (TextView)findViewById(R.id.Amount1);
        Amount2 = (TextView)findViewById(R.id.Amount2);
        Amount3 = (TextView)findViewById(R.id.Amount3);
        Amount4 = (TextView)findViewById(R.id.Amount4);
        Amount5 = (TextView)findViewById(R.id.Amount5);



        DatabaseReference userOrders = databaseReference.child("userOrders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Profile profile = dataSnapshot.getValue(Profile.class);
                past_orders = profile.userOrders.getIndex();
                navName = (TextView)findViewById(R.id.navName);
                navEmail = (TextView)findViewById(R.id.navEmail);
                navName.setText(profile.userProfile.getUserName());
                navEmail.setText(profile.userProfile.getEmail());

                /*navName.setText(profile.userProfile.getUserName());
                System.out.print(profile.userProfile.getEmail());*/


                /*int counter = 0;
                ArrayList<String> arr = new ArrayList<String>();
                int i=1;
                for( DataSnapshot user_orders : dataSnapshot.child("userOrders").child(String.valueOf(i)).getChildren()){
                    arr.add(user_orders.getValue().toString());
                }
                Date1.setText(arr.get(1));
                Amount1.setText(getString(R.string.Rs)+arr.get(0));

                for( DataSnapshot user_orders : dataSnapshot.child("userOrders").child("2").getChildren()){
                    arr.add(user_orders.getValue().toString());
                }counter++;
                Date2.setText(arr.get(3));
                Amount2.setText(getString(R.string.Rs)+arr.get(2));

                for( DataSnapshot user_orders : dataSnapshot.child("userOrders").child("3").getChildren()){
                    arr.add(user_orders.getValue().toString());
                }counter++;
                Date3.setText(arr.get(5));
                Amount3.setText(getString(R.string.Rs)+arr.get(4));

                for( DataSnapshot user_orders : dataSnapshot.child("userOrders").child("4").getChildren()){
                    arr.add(user_orders.getValue().toString());
                }counter++;
                Date4.setText(arr.get(7));
                Amount4.setText(getString(R.string.Rs)+arr.get(6));

                for( DataSnapshot user_orders : dataSnapshot.child("userOrders").child("5").getChildren()){
                    arr.add(user_orders.getValue().toString());
                }counter++;
                Date5.setText(arr.get(9));
                Amount5.setText(getString(R.string.Rs)+arr.get(8));

*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        firebaseAuth = FirebaseAuth.getInstance();

        switch(menuItem.getItemId()){
            case R.id.logout:
                //Toast.makeText(secondary_activity.this,"You clicked!!",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;

            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

        }
        //Close Navigation Drawer
        myDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
