package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Ticket;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

public class ViewTickets extends AppCompatActivity {

    Button back;

    TextView t1,t2,t3,t4,t5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        back=(Button) findViewById(R.id.viewticketback);

        t1=(TextView) findViewById(R.id.viewticketbusno);
        t2=(TextView)findViewById(R.id.viewticketseatno);
        t3=(TextView)findViewById(R.id.viewticketstop);
        t4=(TextView)findViewById(R.id.viewticketadultorchild);
        t5=(TextView)findViewById(R.id.viewticketdate);

        final Session s = new Session(getApplicationContext());

        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String ticketid = savedInstanceState.getString("ticketid");

        Toast.makeText(getApplicationContext(),"TiketID:"+ticketid,Toast.LENGTH_LONG).show();

        DAO d=new DAO();
        d.getDBReference(Constants.Ticket_DB).child(ticketid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Ticket ticket=dataSnapshot.getValue(Ticket.class);

                if(ticket!=null)
                {
                    t1.setText("Bus NO :"+ticket.getBusno());
                    t2.setText("Seat NO :"+ticket.getSeatno());
                    t3.setText("Stop :"+ticket.getStop());
                    t4.setText("User Name :"+ticket.getUserid());
                    t5.setText("Date of Travel :"+ticket.getDt());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),UserHome.class);
                startActivity(i);
            }
        });
    }
}
