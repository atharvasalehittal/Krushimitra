package com.mitra.krushi.krushimitra;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText eEmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        eEmail = (EditText) findViewById(R.id.email_address);
    }

    public void resetPassword(View view) {

        String email = eEmail.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please write valid email id first",Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Please check your email",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"Error Occured"+message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
