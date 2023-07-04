package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Bus;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

import java.util.List;
import java.util.Locale;

public class ViewBus extends AppCompatActivity {

    Button deleteBus;
    Button viewBusBack;

    Button addStops;
    Button viewStops;
    Button bookTicket;

    ImageView imageView;

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;

    String tdate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus);

        deleteBus=(Button) findViewById(R.id.viewbusdelete);
        viewBusBack=(Button) findViewById(R.id.viewbuscancel);
        addStops=(Button) findViewById(R.id.viewbusaddstop);
        viewStops=(Button) findViewById(R.id.viewbusviewstops);
        bookTicket=(Button) findViewById(R.id.viewbusbookticket);

        t1=(TextView) findViewById(R.id.viewbusname);
        t2=(TextView)findViewById(R.id.viewbusnoofseats);
        t3=(TextView)findViewById(R.id.viewbustype);
        t4=(TextView)findViewById(R.id.viewbussource);
        t5=(TextView)findViewById(R.id.viewbusdestination);
        t6=(TextView)findViewById(R.id.viewbusmobile);
        t7=(TextView)findViewById(R.id.viewbusstarttime);
        t8=(TextView)findViewById(R.id.viewbusno);
        t9=(TextView)findViewById(R.id.viewbuslocaton);

        imageView = (ImageView) findViewById(R.id.viewbusimage);

        final Session s = new Session(getApplicationContext());

        Intent i = getIntent();
        savedInstanceState = i.getExtras();

        String role=s.getRole();

        String bid="";

        if(role.equals("admin"))
        {
            bid=savedInstanceState.getString("busid");
        }
        else if (role.equals("user"))
        {
            String received=savedInstanceState.getString("busid");
            Log.v("busid spliting ",received);
            String[] tokens=received.split("@");

            bid=tokens[0];
            tdate=tokens[1];
        }

        final String busid = bid;

        DAO d=new DAO();
        d.getDBReference(Constants.BUSSES_DB).child(busid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Bus bus=dataSnapshot.getValue(Bus.class);

                if(bus!=null)
                {
                    String busAddress="";

                    String latlong=bus.getLatlong();

                    if(latlong!=null)
                    {
                        String[] busLocation=latlong.split(",");

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        try {

                            addresses = geocoder.getFromLocation(new Double(busLocation[0]),new Double(busLocation[1]), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                            if(address!=null)
                            {
                                busAddress=busAddress+address+"\n";
                            }

                            if(city!=null)
                            {
                                busAddress=busAddress+city+"\n";
                            }

                            if(state!=null)
                            {
                                busAddress=busAddress+state+"\n";
                            }

                            if(country!=null)
                            {
                                busAddress=busAddress+country+"\n";
                            }

                            if(postalCode!=null)
                            {
                                busAddress=busAddress+postalCode+"\n";
                            }

                            if(knownName!=null)
                            {
                                busAddress=busAddress+knownName+"\n";
                            }
                        }
                        catch(Exception e)
                        {
                            Log.v("voidmain ","in on succes ");
                        }
                    }

                    t1.setText("Bus Name :"+bus.getBusName());
                    t2.setText("Bus No Seats :"+bus.getNoOfSeats());
                    t3.setText("Bus Type :"+bus.getBusType());
                    t4.setText("Bus Source :"+bus.getSource());
                    t5.setText("Bus Destination :"+bus.getDest());
                    t6.setText("Bus Mobile :"+bus.getMobile());
                    t7.setText("Bus Start Time :"+bus.getStarttime());
                    t8.setText("Bus NO :"+bus.getBusNo());
                    t9.setText("Bus Location :"+busAddress);

                    StorageReference ref = DAO.getStorageReference().child("images/" +bus.getImage());
                    long ONE_MEGABYTE = 1024 * 1024 *5;
                    ref.getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {

                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                    if(bm!=null)
                                    {
                                        imageView.setImageBitmap(bm);
                                    }
                                    else
                                    {
                                        Log.v("voidmain ","bm null");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.v("voidmain ","image reading failure");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(role.equals("user") || role.equals("bus"))
        {
            addStops.setEnabled(false);
            deleteBus.setEnabled(false);
        }

        if(role.equals("admin") || role.equals("bus"))
        {
            bookTicket.setEnabled(false);
        }

        addStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(),AddStop.class);
                i.putExtra("busid", busid);
                startActivity(i);
            }
        });

        viewStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(),ListStops.class);
                i.putExtra("busid", busid);
                startActivity(i);
            }
        });

        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(),BookTicket.class);
                i.putExtra("busid", busid);
                i.putExtra("tdate", tdate);
                startActivity(i);
            }
        });

        deleteBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("deleting object","busid :"+busid);

                final DAO dao=new DAO();
                dao.deleteObject(Constants.BUSSES_DB,busid);
                Intent i= new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
            }
        });

        viewBusBack.setOnClickListener(new View.OnClickListener() {
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
