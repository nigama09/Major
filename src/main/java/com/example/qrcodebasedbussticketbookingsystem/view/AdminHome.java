package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodebasedbussticketbookingsystem.MainActivity;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

public class AdminHome extends AppCompatActivity {

    Button adminLogout;
    Button addBus;
    Button viewBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        addBus=(Button) findViewById(R.id.addbus);
        viewBus=(Button) findViewById(R.id.adminviewbus);
        adminLogout=(Button) findViewById(R.id.adminlogout);

        final Session s = new Session(getApplicationContext());

        addBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("in list view action ","");
                Intent i = new Intent(getApplicationContext(),AddBus.class);
                startActivity(i);
            }
        });

        viewBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("in list view action ","");
                Intent i = new Intent(getApplicationContext(),ListBus.class);
                startActivity(i);
            }
        });

        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s.loggingOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}