package com.example.qrcodebasedbussticketbookingsystem.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodebasedbussticketbookingsystem.MainActivity;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class UserHome extends AppCompatActivity {

    EditText source;
    EditText destination;
    Button usersearchButton;
    Button userlogout;
    Button userViweTickets;

    private EditText dateOfTravel;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        source = (EditText) findViewById(R.id.usersearchsource);
        destination = (EditText) findViewById(R.id.usersearchdestination);

        usersearchButton = (Button) findViewById(R.id.usersearchButton);
        userlogout = (Button) findViewById(R.id.userlogout);
        userViweTickets = (Button) findViewById(R.id.userViewTickets);

        final Session s = new Session(getApplicationContext());

        // calender setting start

        myCalendar = Calendar.getInstance();

        dateOfTravel= (EditText) findViewById(R.id.dateOfTravel);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateOfTravel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UserHome.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        usersearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = source.getText().toString();
                String d = destination.getText().toString();

                Intent i = new Intent(getApplicationContext(), ListBus.class);
                i.putExtra("source", s);
                i.putExtra("destination", d);
                i.putExtra("tdate",dateOfTravel.getText().toString());

                startActivity(i);
            }
        });

        userViweTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListTickets.class);
                startActivity(i);
            }
        });

        userlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s.loggingOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfTravel.setText(sdf.format(myCalendar.getTime()));
    }
}
