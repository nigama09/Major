package com.example.qrcodebasedbussticketbookingsystem.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Bus;
import com.example.qrcodebasedbussticketbookingsystem.form.Stop;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddStop extends AppCompatActivity {

    EditText e1,e2,e3;

    Button submit;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        e1=(EditText)findViewById(R.id.addstopname);
        e2=(EditText)findViewById(R.id.addadultfair);
        e3=(EditText)findViewById(R.id.addchildfair);

        submit=(Button)findViewById(R.id.addstopbutton);
        cancel=(Button)findViewById(R.id.addstopcancelbutton);

        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String busid = savedInstanceState.getString("busid");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DAO dao = new DAO();

                String stopname=e1.getText().toString();
                String adultfair=e2.getText().toString();
                String childfair=e3.getText().toString();

                Stop stop=new Stop();

                stop.setStopid(dao.getUnicKey(Constants.STOPS_DB));
                stop.setStopname(stopname);
                stop.setAudiltfair(adultfair);
                stop.setChildfair(childfair);
                stop.setBusno(busid);

                try {
                    dao.addObject(Constants.STOPS_DB, stop, stop.getStopid());
                    Intent i = new Intent(getApplicationContext(), AdminHome.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Stop Added Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(i);
            }
        });
    }
}
