package com.mitra.krushi.krushimitra;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

public class Navigation extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigationView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mToggle = new ActionBarDrawerToggle(Navigation.this,mDrawerLayout,R.string.Open,R.string.Close);

        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.nav_logout){
                    finish();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Navigation.this,MainActivity.class));
                }

                if (id == R.id.nav_changepassword){
                    finish();
                    startActivity(new Intent(Navigation.this,ChangePassword.class));
                }

                if (id == R.id.market_value){
                  //  finish();
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.msamb.com/ApmcDetail/ArrivalPriceInfo")));
                }

                if (id == R.id.help){
                   // finish();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:18001801551"));
                    startActivity(intent);

                }


                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Display(View view) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String user_name = dataSnapshot.child(currentUser).child("u_name").getValue(String.class);
                String ph_no = dataSnapshot.child(currentUser).child("ph_no").getValue(String.class);
                String district = dataSnapshot.child(currentUser).child("u_district").getValue(String.class);
                Double phValue = dataSnapshot.child(currentUser).child("u_ph").getValue(Double.class);
                Double ecValue = dataSnapshot.child(currentUser).child("u_ec").getValue(Double.class);
                Double ocValue = dataSnapshot.child(currentUser).child("u_oc").getValue(Double.class);
                String soilType = dataSnapshot.child(currentUser).child("u_soil_type").getValue(String.class);

                TextView textView=(TextView) findViewById(R.id.disp);
                textView.setText(phValue.toString());
                User user=new User(user_name,district,soilType,phValue,ecValue,ocValue,ph_no);
                Intent intent = new Intent(Navigation.this,login.class).putExtra("name",user);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.i("Navigation","OnCancelled");
            }
        });
    }

}
