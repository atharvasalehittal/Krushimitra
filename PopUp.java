package com.mitra.krushi.krushimitra;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUp extends Activity {

    static int prevcount;
    SuggestFertil fertil;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4));


        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity=Gravity.CENTER;
        params.x=0;
        params.y=-20;

        getWindow().setAttributes(params);

        fertil=(SuggestFertil) getIntent().getSerializableExtra("name");

        TextView textView=findViewById(R.id.text1);
        textView.setText(fertil.getCropName());

        String str="Fertilizer Suggestions are:" +
                "\nFertilizer for pH:- "+fertil.getFertilizPH() +
                "\nFertilizer for eC:- "+fertil.getFertilizEC() +
                "\nFertilizer for oC:- "+fertil.getFertilizOC();
        TextView textView1=findViewById(R.id.text2);
        textView1.setText(str);

        button = (Button) findViewById(R.id.button1);
    }

    /*@Override
    protected void onResume() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean enabled = sharedPreferences.getBoolean("isEnabled",true);
        button.setEnabled(enabled);
        super.onResume();
    }*/

    public void selectMethod(View view) {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("User_Crop").child(fertil.getCropName()).getValue().toString();
                final int count = Integer.parseInt(value);
                prevcount = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("User_Crop").child(fertil.getCropName()).setValue(prevcount+1);

       //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       //boolean enabled = sharedPreferences.edit().putBoolean("isEnabled",false).commit();
       //button.setEnabled(false);


        //button.setVisibility(View.INVISIBLE);
    }

    public void closeMethod(View view) {
        finish();
    }
}