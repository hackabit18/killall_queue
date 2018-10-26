package com.killall_queuehackabit.smartshop;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.killall_queuehackabit.smartshop.menu.DrawerAdapter;
import com.killall_queuehackabit.smartshop.menu.DrawerItem;
import com.killall_queuehackabit.smartshop.menu.SimpleItem;
import com.killall_queuehackabit.smartshop.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,DrawerAdapter.OnItemSelectedListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    private TextView Date1,Date2,Date3,Date4,Date5,Amount1,Amount2,Amount3,Amount4,Amount5,navInfo,navtitle;
    private ImageView navImg;
    private LinearLayout orderList;
    private int past_orders;

    private SlidingRootNav slidingRootNav;
    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_ABOUT = 2;
    private static final int POS_LOGOUT = 4;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter drawadapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_ABOUT),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        drawadapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(drawadapter);

        drawadapter.setSelected(POS_DASHBOARD);

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



        /*DatabaseReference userOrders = databaseReference.child("userOrders");

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



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });*/

        navtitle = (TextView)findViewById(R.id.navtitle);
        navInfo = (TextView)findViewById(R.id.navInfo);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/RobotoMono-Regular.ttf");
        navtitle.setTypeface(typeface);
        navInfo.setTypeface(typeface);

    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        if (position == POS_ACCOUNT) {
               Intent i =new Intent(getApplication(), ProfileActivity.class);
               startActivity(i);
        }


        if (position == POS_ABOUT) {

        }

        slidingRootNav.closeMenu();
        // Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        // showFragment(selectedScreen);
    }



    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.colorAccent))
                .withTextTint(color(R.color.colorAccent))
                .withSelectedIconTint(color(R.color.colorPrimary))
                .withSelectedTextTint(color(R.color.colorPrimary));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }
    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
