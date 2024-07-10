package com.mitra.krushi.krushimitra;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText eUserName;
    EditText ePassword;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eUserName = (EditText) findViewById(R.id.user_name);
        ePassword = (EditText) findViewById(R.id.pass_word);

    }



    public void signup(View view){
        //finish();
        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        startActivity(intent);
    }

    public void login(View view){

        final String emailId = eUserName.getText().toString();
        String password = ePassword.getText().toString();

        if (emailId.isEmpty()){
            eUserName.setError("Email-Id is required");
            eUserName.requestFocus();
            return;
        }

        if (password.isEmpty()){
            ePassword.setError("Password is required");
            ePassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            eUserName.setError("Please enter a valid email id");
            eUserName.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //finish();
                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,Navigation.class));
                }
                else {
                    Toast.makeText(MainActivity.this,"InCorrect EmailAddress Or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void forget_password(View view) {
        startActivity(new Intent(this,ForgetPassword.class));
    }

}