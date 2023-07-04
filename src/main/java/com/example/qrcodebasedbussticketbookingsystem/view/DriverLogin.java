package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Bus;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

public class DriverLogin extends AppCompatActivity {

    private Session session;
    EditText e1,e2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());

        setContentView(R.layout.activity_driver_login);

        e1=(EditText)findViewById(R.id.driverusername);
        e2=(EditText)findViewById(R.id.driverpassword);
        b1=(Button)findViewById(R.id.driverlogin);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username=e1.getText().toString();
                final String password=e2.getText().toString();

                if(username==null|| password==null || username.length()<=0|| password.length()<=0)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter UserName and Password",Toast.LENGTH_SHORT).show();
                }
                else {

                    DAO d = new DAO();
                    d.getDBReference(Constants.BUSSES_DB).child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Bus bus=dataSnapshot.getValue(Bus.class);

                            if (bus == null) {
                                Toast.makeText(getApplicationContext(), "Invalid UserName ", Toast.LENGTH_SHORT).show();
                            } else if (bus != null && bus.getPassword().equals(password)) {

                                session.setusername(username);
                                session.setRole("bus");

                                Intent  i = new Intent(getApplicationContext(), UpdateBusLocation.class);
                                startActivity(i);

                            } else {
                                Toast.makeText(getApplicationContext(), "In valid Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
