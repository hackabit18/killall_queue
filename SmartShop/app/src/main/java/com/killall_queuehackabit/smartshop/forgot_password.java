package com.killall_queuehackabit.smartshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    private EditText forgot_email;
    private Button enter_forgot_email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgot_email = (EditText)findViewById(R.id.forgot_email);
        enter_forgot_email = (Button)findViewById(R.id.enter_forgot_email);
        firebaseAuth = FirebaseAuth.getInstance();

        enter_forgot_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgot_email.getText().toString().trim();

                if(email.equals("")){
                    Toast.makeText(forgot_password.this,"Please Enter your email to continue",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgot_password.this,"Password Reset Link Sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgot_password.this, MainActivity.class));
                            }else{
                                Toast.makeText(forgot_password.this,"Error! No account found."+enter_forgot_email,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
