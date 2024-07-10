package com.mitra.krushi.krushimitra;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class login extends AppCompatActivity {

    int n_diff=0;
    public int f_rain=0;
    ArrayList<Crop> acc_crops=new ArrayList<Crop>();
    ArrayList<SuggestFertil> finalDisp = new ArrayList<SuggestFertil>();
    String u_district;
    String str[] = new String[5];

    static int prevcount;
    //Get User from Database
    User user;
   // User user=new User("nikhil","Satara","Black",1.2,1.2,1.3,"1234567890");
    ArrayList<Difference> diff=new ArrayList<Difference>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         get_user();
         calculate_rain();
         soil_filter();
         create_diff();
         sort_diff();


    }


    void get_user()
    {

        //String u_name;
        //String u_soil_type;
        //double u_ph;
        //double u_ec;
        //double u_oc;
        //String ph_no;



        /*String[] s1=s.split(",");

        TextView textView= findViewById(R.id.s_diff);
        textView.setText(s);


        String[] s_ec=s1[0].split("=");
        u_ec=new Double(s_ec[1]);

        String[] s_ph=s1[1].split("=");
        ph_no=s_ph[1];

        String[] s_n=s1[2].split("=");
        u_name=s_n[1];

        String[] s_oc=s1[3].split("=");
        u_oc=new Double(s_oc[1]);

        String[] s_d=s1[4].split("=");
        u_district=s_d[1];

        String[] s_s=s1[5].split("=");
        u_soil_type=s_s[1];

        String[] s_p=s1[6].split("=");
        u_ph=new Double(s_p[1]);
        */

        user = (User) getIntent().getSerializableExtra("name");
        u_district=user.getU_district();
        //TextView textView= findViewById(R.id.s_diff);
        //textView.setText(user.getU_name());
    }


    void calculate_rain()
    {
        String str;

        double prev_avg1=0;
        double prev_avg2=0;
        double prev_avg3=0;
        double prev_avg4=0;
        double pres=0;

        String[] args={u_district};

        waterdb db=new waterdb(login.this);
        Cursor cursor=db.query(null,"dist=?",args,db.col1);

        if(cursor.moveToFirst()){
            //String[] dist=new String[100];
            do{
                //cursor.getString(cursor.getColumnIndex(db.col1));
                prev_avg1=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col2)));
                prev_avg2=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col3)));
                prev_avg3=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col4)));
                prev_avg4=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col5)));
                pres=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col6)));
            }while(cursor.moveToNext());
            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_LONG).show();
        }



        double inc=-getavg(4, prev_avg1-pres, prev_avg2-prev_avg1,prev_avg3-prev_avg2,prev_avg4-prev_avg3);

        f_rain=(int) (pres+(pres*inc/100));

        //TextView textView=(TextView) findViewById(R.id.r_val);
        //str="Final rain is"+f_rain;
        //textView.setText(str);
    }
    double getavg(int n,double d1,double d2,double d3,double d4)
    {
        return (double) (d1+d2+d3+d4)/n;
    }

    double getavg1(int n,double d1,double d2,double d3,double d4,double d5)
    {
        return (double) (d1+d2+d3+d4+d5)/n;
    }

    void soil_filter()
    {
        //Creating Arraylist of All Crops (Get from Database)
        ArrayList<Crop> crops=new ArrayList<Crop>();
        /*crops.add(new Crop("Jowar",200,"Black",2.5,1.5,1.0));
        crops.add(new Crop("Bajra",100,"Black",3.0,2.0,1.5));
        crops.add(new Crop("Rice",200,"Red",2.0,1.0,0.5));
        */

        cropdb db=new cropdb(login.this);

        Cursor cursor=db.query(null,null,null,db.col1);
       /* ArrayList al=new ArrayList();
        ArrayList a2=new ArrayList();
        ArrayList a3=new ArrayList();
        ArrayList a4=new ArrayList();
        ArrayList a5=new ArrayList();
        ArrayList a6=new ArrayList();
        */
       String name;
       String soiltype;
       int rain;
       double ph;
       double ec;
       double oc;
        if(cursor.moveToFirst()){
            String[] dist=new String[100];
            do{
                name=cursor.getString(cursor.getColumnIndex(db.col1));
                rain=Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.col2)));
                soiltype=cursor.getString(cursor.getColumnIndex(db.col3));
                ph=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col4)));
                ec=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col5)));
                oc=Double.parseDouble(cursor.getString(cursor.getColumnIndex(db.col6)));
                crops.add(new Crop(name,rain,soiltype,ph,ec,oc));
            }while(cursor.moveToNext());

            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_LONG).show();
        }










        //Soil Filter



        Iterator itr= crops.iterator();
        while(itr.hasNext())
        {
            Crop crop=(Crop) itr.next();
            if(user.getU_soil_type().equals(crop.getC_ess_soil()))
            {
                acc_crops.add(crop);
            }
        }


        Iterator itr1=acc_crops.iterator();
        String str=null;
        while (itr1.hasNext())
        {
            Crop crop=(Crop) itr1.next();
            str=str+crop.getC_name();
        }
        //TextView textView1=(TextView) findViewById(R.id.acc_crops);
        //str="Crops: "+str;
        //textView1.setText(str);
    }
    void create_diff()
    {
        Crop c;
        Difference d;
        Iterator itr=acc_crops.iterator();
        while (itr.hasNext())
        {
            c=(Crop) itr.next();
            diff.add(new Difference(c.getC_name(),f_rain-c.getC_ess_rain(),user.getU_ph()-c.getC_ess_ph(),user.getU_ec()-c.getC_ess_ec(),user.getU_oc()-c.getC_ess_oc()));
        }
        itr=diff.iterator();
        String str=null;
        while(itr.hasNext())
        {
            d=(Difference) itr.next();
            str=str+"\n"+d.getD_cname()+"\tr_diff:\t"+d.getD_rdiff()+"\tph_diff:\t"+d.getD_phdiff()+"\tec_diff:\t"+d.getD_ecdiff()+"\toc_diff:\t"+d.getD_ocdiff()+"\n";
        }
        //TextView textView= findViewById(R.id.diff);
        //textView.setText(str);
    }
    void sort_diff() {
        int n = 0;
        int i = 0;
        int j = 0;
        Difference[] difference = new Difference[50];
        Iterator itr = diff.iterator();
        while (itr.hasNext()) {
            difference[i] = ((Difference) itr.next());
            ++i;
        }
        n = i;
        Difference temp;
        for (i = 0; i < n; i++) {
            for (j = i + 1; j < n; j++) {
                if (difference[i].getD_rdiff() > difference[j].getD_rdiff()) {
                    temp = difference[i];
                    difference[i] = difference[j];
                    difference[j] = temp;
                }
            }
        }
        diff.clear();
        for (i = 0; i < n; i++) {
            diff.add(difference[i]);
        }

        Difference d;
        itr = diff.iterator();
        ;
        int count = 0;
        while (itr.hasNext() && count < 5) {
            d = (Difference) itr.next();
            str[count] = d.getD_cname();
            count++;
        }

        TextView textView1 = findViewById(R.id.crop1);
        textView1.setText(str[0]);
        TextView textView2 = findViewById(R.id.crop2);
        textView2.setText(str[1]);
        TextView textView3 = findViewById(R.id.crop3);
        textView3.setText(str[2]);
        TextView textView4 = findViewById(R.id.crop4);
        textView4.setText(str[3]);
        TextView textView5 = findViewById(R.id.crop5);
        textView5.setText(str[4]);


        String cropName, phHigh, phLow, ecHigh, ecLow, ocHigh, ocLow;
        suggestdb db = new suggestdb(login.this);

        String args1[] = {diff.get(0).c_name};//, diff.get(1).c_name, diff.get(2).c_name, diff.get(3).c_name, diff.get(4).c_name


        Cursor cursor = db.query(null, "cropname=?", args1, db.col1);


        if (cursor.moveToFirst()) {
            do {
                cropName = cursor.getString(cursor.getColumnIndex(db.col1));
                phHigh = cursor.getString(cursor.getColumnIndex(db.col2));
                phLow = cursor.getString(cursor.getColumnIndex(db.col3));
                ecHigh = cursor.getString(cursor.getColumnIndex(db.col4));
                ecLow = cursor.getString(cursor.getColumnIndex(db.col5));
                ocHigh = cursor.getString(cursor.getColumnIndex(db.col6));
                ocLow = cursor.getString(cursor.getColumnIndex(db.col7));

                finalDisp.add(new SuggestFertil(cropName, phHigh, phLow, ecHigh, ecLow, ocHigh, ocLow, diff.get(0).ph_diff, diff.get(0).ec_diff, diff.get(0).oc_diff));
            } while (cursor.moveToNext());
        }



            String args2[] = {diff.get(1).c_name};
            cursor = db.query(null, "cropname=?", args2, db.col1);


            if (cursor.moveToFirst()) {
                do {
                    cropName = cursor.getString(cursor.getColumnIndex(db.col1));
                    phHigh = cursor.getString(cursor.getColumnIndex(db.col2));
                    phLow = cursor.getString(cursor.getColumnIndex(db.col3));
                    ecHigh = cursor.getString(cursor.getColumnIndex(db.col4));
                    ecLow = cursor.getString(cursor.getColumnIndex(db.col5));
                    ocHigh = cursor.getString(cursor.getColumnIndex(db.col6));
                    ocLow = cursor.getString(cursor.getColumnIndex(db.col7));

                    finalDisp.add(new SuggestFertil(cropName, phHigh, phLow, ecHigh, ecLow, ocHigh, ocLow, diff.get(0).ph_diff, diff.get(0).ec_diff, diff.get(0).oc_diff));
                } while (cursor.moveToNext());
            }


                String args3[] = {diff.get(2).c_name};
                cursor = db.query(null, "cropname=?", args3, db.col1);


                if (cursor.moveToFirst()) {
                    do {
                        cropName = cursor.getString(cursor.getColumnIndex(db.col1));
                        phHigh = cursor.getString(cursor.getColumnIndex(db.col2));
                        phLow = cursor.getString(cursor.getColumnIndex(db.col3));
                        ecHigh = cursor.getString(cursor.getColumnIndex(db.col4));
                        ecLow = cursor.getString(cursor.getColumnIndex(db.col5));
                        ocHigh = cursor.getString(cursor.getColumnIndex(db.col6));
                        ocLow = cursor.getString(cursor.getColumnIndex(db.col7));

                        finalDisp.add(new SuggestFertil(cropName, phHigh, phLow, ecHigh, ecLow, ocHigh, ocLow, diff.get(0).ph_diff, diff.get(0).ec_diff, diff.get(0).oc_diff));
                    } while (cursor.moveToNext());
                }


                    String args4[] = {diff.get(3).c_name};
                    cursor = db.query(null, "cropname=?", args4, db.col1);


                    if (cursor.moveToFirst()) {
                        do {
                            cropName = cursor.getString(cursor.getColumnIndex(db.col1));
                            phHigh = cursor.getString(cursor.getColumnIndex(db.col2));
                            phLow = cursor.getString(cursor.getColumnIndex(db.col3));
                            ecHigh = cursor.getString(cursor.getColumnIndex(db.col4));
                            ecLow = cursor.getString(cursor.getColumnIndex(db.col5));
                            ocHigh = cursor.getString(cursor.getColumnIndex(db.col6));
                            ocLow = cursor.getString(cursor.getColumnIndex(db.col7));

                            finalDisp.add(new SuggestFertil(cropName, phHigh, phLow, ecHigh, ecLow, ocHigh, ocLow, diff.get(0).ph_diff, diff.get(0).ec_diff, diff.get(0).oc_diff));
                        } while (cursor.moveToNext());
                    }


                        String args5[] = {diff.get(4).c_name};
                        cursor = db.query(null, "cropname=?", args5, db.col1);


                        if (cursor.moveToFirst()) {
                            do {
                                cropName = cursor.getString(cursor.getColumnIndex(db.col1));
                                phHigh = cursor.getString(cursor.getColumnIndex(db.col2));
                                phLow = cursor.getString(cursor.getColumnIndex(db.col3));
                                ecHigh = cursor.getString(cursor.getColumnIndex(db.col4));
                                ecLow = cursor.getString(cursor.getColumnIndex(db.col5));
                                ocHigh = cursor.getString(cursor.getColumnIndex(db.col6));
                                ocLow = cursor.getString(cursor.getColumnIndex(db.col7));

                                finalDisp.add(new SuggestFertil(cropName, phHigh, phLow, ecHigh, ecLow, ocHigh, ocLow, diff.get(0).ph_diff, diff.get(0).ec_diff, diff.get(0).oc_diff));
                            } while (cursor.moveToNext());
                        }

        /*
            SuggestFertil f;
            itr=finalDisp.iterator();
            String str1=null;
            while(itr.hasNext())
            {
                f=(SuggestFertil) itr.next();
                str=str1+"\n"+f.getCropName();
            }

            TextView textView= findViewById(R.id.s_diff);
            textView.setText(str1);


            Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_LONG).show();
        }
        */


            //TextView textView= findViewById(R.id.s_diff);
            //textView.setText(str);


    }

    public void fert1(View view) {
        SuggestFertil fertil=finalDisp.get(0);
        Intent intent=new Intent(login.this,PopUp.class).putExtra("name",fertil);
        startActivity(intent);
    }

    public void fert2(View view) {
        SuggestFertil fertil=finalDisp.get(1);
        Intent intent=new Intent(login.this,PopUp.class).putExtra("name",fertil);
        startActivity(intent);
    }

    public void fert3(View view) {
        SuggestFertil fertil=finalDisp.get(2);
        Intent intent=new Intent(login.this,PopUp.class).putExtra("name",fertil);
        startActivity(intent);
    }

    public void fert4(View view) {
        SuggestFertil fertil=finalDisp.get(3);
        Intent intent=new Intent(login.this,PopUp.class).putExtra("name",fertil);
        startActivity(intent);
    }

    public void fert5(View view) {
        SuggestFertil fertil=finalDisp.get(4);
        Intent intent=new Intent(login.this,PopUp.class).putExtra("name",fertil);
        startActivity(intent);
    }

    public void createGraph(View view) {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("User_Crop").child(str[0]).getValue().toString();
                final int count = Integer.parseInt(value);
                prevcount = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String s1= String.valueOf(prevcount);


        Intent intent = new Intent(login.this,chartActivity.class).putExtra("crop",str[0]).putExtra("crop_cnt",s1);
        startActivity(intent);
    }

    public void createGraph1(View view) {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("User_Crop").child(str[1]).getValue().toString();
                final int count = Integer.parseInt(value);
                prevcount = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String s1= String.valueOf(prevcount);

        Intent intent = new Intent(login.this,chartActivity.class).putExtra("crop",str[1]).putExtra("crop_cnt",s1);
        startActivity(intent);
    }

    public void createGraph2(View view) {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("User_Crop").child(str[2]).getValue().toString();
                final int count = Integer.parseInt(value);
                prevcount = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String s1= String.valueOf(prevcount);

        Intent intent = new Intent(login.this,chartActivity.class).putExtra("crop",str[2]).putExtra("crop_cnt",s1);
        startActivity(intent);
    }

    public void createGraph3(View view) {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("User_Crop").child(str[3]).getValue().toString();
                final int count = Integer.parseInt(value);
                prevcount = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String s1= String.valueOf(prevcount);

        Intent intent = new Intent(login.this,chartActivity.class).putExtra("crop",str[3]).putExtra("crop_cnt",s1);
        startActivity(intent);
    }

    public void createGraph4(View view) {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("User_Crop").child(str[4]).getValue().toString();
                final int count = Integer.parseInt(value);
                prevcount = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String s1= String.valueOf(prevcount);

        Intent intent = new Intent(login.this,chartActivity.class).putExtra("crop",str[4]).putExtra("crop_cnt",s1);
        startActivity(intent);
    }
}