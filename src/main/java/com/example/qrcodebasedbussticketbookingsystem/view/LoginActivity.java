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
import com.example.qrcodebasedbussticketbookingsystem.form.User;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

public class LoginActivity extends AppCompatActivity {

    private Session session;
    EditText e1,e2;
    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());

        setContentView(R.layout.activity_login);

        e1=(EditText)findViewById(R.id.loginPhone);
        e2=(EditText)findViewById(R.id.loginPass);
        b1=(Button)findViewById(R.id.loginConfirm);
        b2=(Button)findViewById(R.id.busLoginSubmit);

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

                    if (username.equals("admin") && password.equals("admin")) {

                        session.setusername("admin");
                        session.setRole("admin");

                        Intent i = new Intent(getApplicationContext(), AdminHome.class);
                        startActivity(i);

                    } else {

                        DAO d = new DAO();
                        d.getDBReference(Constants.USER_DB).child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User user = (User) dataSnapshot.getValue(User.class);

                                if (user == null) {
                                    Toast.makeText(getApplicationContext(), "Invalid UserName ", Toast.LENGTH_SHORT).show();
                                } else if (user != null && user.getPassword().equals(password)) {

                                    session.setusername(user.getUsername());
                                    session.setRole("user");

                                    Intent  i= new Intent(getApplicationContext(), UserHome.class);
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
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),DriverLogin.class);
                startActivity(intent);
            }
        });
    }
}
