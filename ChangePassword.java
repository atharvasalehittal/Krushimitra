package com.mitra.krushi.krushimitra;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    EditText eOldPassword;
    EditText eRepeatChangePassword;
    EditText eChangePassword;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        eOldPassword = (EditText) findViewById(R.id.oldpassword);
        eChangePassword = (EditText) findViewById(R.id.newpassword);
        eRepeatChangePassword = (EditText) findViewById(R.id.newpassword1);
    }

    public void changePassword(View view) {

        if (user != null){
            String oldPassword = eOldPassword.getText().toString();
            final String changePassword = eChangePassword.getText().toString();
            String repeatChangePassword = eRepeatChangePassword.getText().toString();

            if (!changePassword.equalsIgnoreCase(repeatChangePassword)){
                eRepeatChangePassword.setError("Entered password do match");
                eRepeatChangePassword.requestFocus();
                return;
            }

            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(EmailAuthProvider.getCredential(user.getEmail(),oldPassword)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Reauthenticated",Toast.LENGTH_SHORT).show();
                        user.updatePassword(changePassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ChangePassword.this,Navigation.class));
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"User's Entered Password Not Correct",Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
    }
}
