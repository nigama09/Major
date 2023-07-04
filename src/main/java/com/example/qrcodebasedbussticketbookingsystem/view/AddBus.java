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
import android.util.Log;
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
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddBus extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    Button addbussubmitButton;
    Button addbussubmitCancel;

    Button btnChoose;

    private static final int SELECT_PICTURE = 100;
    private Uri imageUri1;

    Spinner busType;
    String busTypeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        e1=(EditText)findViewById(R.id.addbusname);
        e2=(EditText)findViewById(R.id.addbusnoofseats);
        e3=(EditText)findViewById(R.id.addbussource);
        e4=(EditText)findViewById(R.id.addbusdestination);
        e5=(EditText)findViewById(R.id.addbusmobile);
        e6=(EditText)findViewById(R.id.addbusstarttime);
        e7=(EditText)findViewById(R.id.addbuspassword);
        e8=(EditText)findViewById(R.id.addbusnumber);

        addbussubmitButton=(Button)findViewById(R.id.addbussubmitButton);
        addbussubmitCancel=(Button)findViewById(R.id.addbussubmitCancel);

        btnChoose = (Button) findViewById(R.id.addbuschooseimagebutton);
        busType=(Spinner) findViewById(R.id.spinnerBusType);

        List<String> al=new ArrayList<>();
        al.add("AC");
        al.add("Non AC");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(busType.getContext(),
                android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

        busType.setAdapter(adapter);

        // Spinner click listener
        busType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                busTypeString= adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        addbussubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DAO dao = new DAO();

                String busname=e1.getText().toString();
                String noofseats=e2.getText().toString();
                String source=e3.getText().toString();
                String destination=e4.getText().toString();
                String mobile=e5.getText().toString();
                String starttime=e6.getText().toString();
                String password=e7.getText().toString();
                String busno=e8.getText().toString();

                Bus bus=new Bus();

                bus.setBusNo(busno);
                bus.setBusName(busname);
                bus.setNoOfSeats(noofseats);
                bus.setSource(source);
                bus.setDest(destination);
                bus.setMobile(mobile);
                bus.setStarttime(starttime);
                bus.setPassword(password);
                bus.setBusType(busTypeString);

                String imageName = UUID.randomUUID().toString();
                bus.setImage(imageName);
                uploadImage(imageName);

                try {
                    dao.addObject(Constants.BUSSES_DB, bus, bus.getBusNo());
                    Intent i = new Intent(getApplicationContext(), AdminHome.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Bus Added Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }

            }
        });

        addbussubmitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == SELECT_PICTURE) {

                        imageUri1= data.getData();

                        if (null != imageUri1) {

                            findViewById(R.id.addbusimage).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.addbusimage)).setImageURI(imageUri1);
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    private void uploadImage(String fileName) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        Log.v("uploading ..... ",fileName);

        StorageReference ref = DAO.getStorageReference().child("images/" + fileName);

        ref.putFile(imageUri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
    }
}
