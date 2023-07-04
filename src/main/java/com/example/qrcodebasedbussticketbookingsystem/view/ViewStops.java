package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Stop;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;


public class ViewStops extends AppCompatActivity {

    Button deleteStop;
    Button viewStopBack;

    TextView t1,t2,t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stops);

        deleteStop=(Button) findViewById(R.id.viewstopdelete);
        viewStopBack=(Button) findViewById(R.id.viewstopcancel);

        t1=(TextView) findViewById(R.id.viewstopname);
        t2=(TextView)findViewById(R.id.viewadultfair);
        t3=(TextView)findViewById(R.id.viewchildfair);

        final Session s = new Session(getApplicationContext());

        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String stopid = savedInstanceState.getString("stopid");

        String role=s.getRole();

        DAO d=new DAO();
        d.getDBReference(Constants.STOPS_DB).child(stopid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Stop stop=dataSnapshot.getValue(Stop.class);

                if(stop!=null)
                {
                    t1.setText("Stop Name :"+stop.getStopname());
                    t2.setText("Adult Fair :"+stop.getAudiltfair());
                    t3.setText("Child Fair :"+stop.getChildfair());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(role.equals("user") || role.equals("bus"))
        {
            deleteStop.setEnabled(false);
        }

        deleteStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DAO dao=new DAO();
                dao.deleteObject(Constants.STOPS_DB,stopid);

                Intent i= new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
            }
        });

        viewStopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=null;

                String role=s.getRole();

                if(role.equals("admin"))
                {
                    i= new Intent(getApplicationContext(),AdminHome.class);
                }else if(role.equals("bus"))
                {
                    i= new Intent(getApplicationContext(),UpdateBusLocation.class);
                }else if(role.equals("user"))
                {
                    i= new Intent(getApplicationContext(),UserHome.class);
                }
                startActivity(i);
            }
        });
    }
}
