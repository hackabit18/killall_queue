package com.killall_queuehackabit.smartshop;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.killall_queuehackabit.smartshop.R;

import java.util.ArrayList;
import java.util.Random;

public class cart extends ListActivity {
    private Button addBtn;
    private String value;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    Random rand = new Random();
    Handler handler = new Handler();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_cart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        Runnable r=new Runnable() {
            public void run() {
                int v = rand.nextInt(10);
                adapter=new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        listItems);
                addBtn = (Button)findViewById(R.id.addBtn);


                listItems.clear();
                DatabaseReference databaseReference = firebaseDatabase.getReference("7412350101");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            for(DataSnapshot ds : snapshot.getChildren()){
                                value = ds.getValue().toString();
                                addItems(addBtn);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                setListAdapter(adapter);
                handler.postDelayed(this, 16000);
            }
        };

        handler.postDelayed(r, 500);

    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(View v) {
        listItems.add( ""+value);
        //Toast.makeText(cart.this,"New Item added in the cart!",Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }
}