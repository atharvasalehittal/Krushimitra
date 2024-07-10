package com.mitra.krushi.krushimitra;

import android.content.Intent;
import android.database.Cursor;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private static int i = 1;
    private EditText eName;
    private EditText ePhoneNumber;
    private EditText eEmailId;
    private EditText ePassword;
    private EditText eConfirmPassword;
    private EditText epH;
    private EditText eEC;
    private EditText eOC;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        eName = (EditText) findViewById(R.id.name);
        ePhoneNumber = (EditText) findViewById(R.id.phone_number);
        eEmailId = (EditText) findViewById(R.id.email);
        ePassword = (EditText) findViewById(R.id.password);
        eConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        epH = (EditText) findViewById(R.id.pH);
        eEC = (EditText) findViewById(R.id.EC);
        eOC = (EditText) findViewById(R.id.OC);

    }

    /*public void login(View view){
        startActivity(new Intent(SignUp.this,MainActivity.class));
    }*/

    public void register(View view){

        double pH = 0.00;
        double EC = 0.00;
        double OC = 0.00;

        String name = eName.getText().toString();
        String phoneNumber = ePhoneNumber.getText().toString();
        String emailId = eEmailId.getText().toString();
        String password = ePassword.getText().toString();
        String confirmPassword = eConfirmPassword.getText().toString();
        String SpH = epH.getText().toString();
        String SEC = eEC.getText().toString();
        String SOC = eOC.getText().toString();
        final String pCode = getIntent().getStringExtra("name");
        String tdigitCode = pCode.substring(0,3);

        if (name.isEmpty()){
            eName.setError("Name is required");
            eName.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()){
            ePhoneNumber.setError("PhoneNumber is required");
            ePhoneNumber.requestFocus();
            return;
        }

        if (emailId.isEmpty()){
            eEmailId.setError("Email-Id is required");
            eEmailId.requestFocus();
            return;
        }

        if (password.isEmpty()){
            ePassword.setError("Password is required");
            ePassword.requestFocus();
            return;
        }

        if (SpH.isEmpty()){
            soildb db=new soildb(SignUp.this);
            String[] args={tdigitCode};
            String dist_ph=null;

            Cursor cursor=db.query(null,"dist=?",args,db.col1);
            if(cursor.moveToFirst()){
                String[] dist=new String[100];
                do{
                    //cursor.getString(cursor.getColumnIndex(db.col1));
                    dist_ph=cursor.getString(cursor.getColumnIndex(db.col3));
                }while(cursor.moveToNext());
                pH= Double.parseDouble(dist_ph);
                Toast.makeText(getApplicationContext(),dist_ph,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
            }
        }

        if (SEC.isEmpty()){
            soildb db=new soildb(SignUp.this);
            String[] args={tdigitCode};
            String dist_ec=null;

            Cursor cursor=db.query(null,"dist=?",args,db.col1);
            if(cursor.moveToFirst()){
                String[] dist=new String[100];
                do{
                    //cursor.getString(cursor.getColumnIndex(db.col1));
                    dist_ec=cursor.getString(cursor.getColumnIndex(db.col4));
                }while(cursor.moveToNext());
                EC= Double.parseDouble(dist_ec);
                Toast.makeText(getApplicationContext(),dist_ec,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
            }
        }

        if (SOC.isEmpty()){
            soildb db=new soildb(SignUp.this);
            String[] args={tdigitCode};
            String dist_oc=null;

            Cursor cursor=db.query(null,"dist=?",args,db.col1);
            if(cursor.moveToFirst()){
                String[] dist=new String[100];
                do{
                    //cursor.getString(cursor.getColumnIndex(db.col1));
                    dist_oc=cursor.getString(cursor.getColumnIndex(db.col5));
                }while(cursor.moveToNext());
                OC= Double.parseDouble(dist_oc);
                Toast.makeText(getApplicationContext(),dist_oc,Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
            }
        }

        if (phoneNumber.length() < 10 || phoneNumber.length() > 10){
            ePhoneNumber.setError("Enter a valid PhoneNumber");
            ePhoneNumber.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
            eEmailId.setError("Please enter a valid email id");
            eEmailId.requestFocus();
            return;
        }

        if (password.length() < 6){
            ePassword.setError("Minimum length of password is 6");
            ePassword.requestFocus();
            return;
        }

        if (!password.equalsIgnoreCase(confirmPassword)){
            eConfirmPassword.setError("Password do not match.Please enter password again");
            eConfirmPassword.requestFocus();
            return;
        }

        if (!SpH.isEmpty()){
            try {
                pH = Double.parseDouble(SpH);
            }
            catch (Exception e){
                epH.setError("Entered input wrong");
                epH.requestFocus();
                return;
            }
        }


        if (!SEC.isEmpty()){
            try {
                EC = Double.parseDouble(SEC);
            }
            catch (Exception e){
                eEC.setError("Entered input wrong");
                eEC.requestFocus();
                return;
            }
        }


        if (!SOC.isEmpty()){
            try {
                OC = Double.parseDouble(SOC);
            }
            catch (Exception e){
                eOC.setError("Entered input wrong");
                eOC.requestFocus();
                return;
            }
        }

        soildb db=new soildb(SignUp.this);
        String[] args={tdigitCode};
        String soil_type=null;

        Cursor cursor=db.query(null,"dist=?",args,db.col1);
        if(cursor.moveToFirst()){
            String[] dist=new String[100];
            do{
                //cursor.getString(cursor.getColumnIndex(db.col1));
                soil_type=cursor.getString(cursor.getColumnIndex(db.col2));
            }while(cursor.moveToNext());
            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_LONG).show();
        }




        final User user = new User(name,tdigitCode,soil_type,pH,EC,OC,phoneNumber);

        mAuth.createUserWithEmailAndPassword(emailId,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //finish();
                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"User Not Registered",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //startActivity(new Intent(SignUp.this,MainActivity.class));
    }
}
