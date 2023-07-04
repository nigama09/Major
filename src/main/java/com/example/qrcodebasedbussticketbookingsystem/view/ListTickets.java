package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Ticket;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.MapUtil;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ListTickets extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tickets);

        listView=(ListView) findViewById(R.id.TicketList);

        final Session s=new Session(getApplicationContext());

        final Map<String,String> viewMap=new HashMap<String,String>();

        DAO d=new DAO();
        d.getDBReference(Constants.Ticket_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                    Ticket ticket=(Ticket)snapshotNode.getValue(Ticket.class);

                    if(ticket.getUserid().equals(s.getusername())) {

                        viewMap.put(ticket.getSeatno() + "-" + ticket.getBusno(), ticket.getTicketId());
                    }
                }

                ArrayList<String> al=new ArrayList<String>(viewMap.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));
                listView.setAdapter(adapter);

                Session s=new Session(getApplicationContext());
                s.setViewMap(MapUtil.mapToString(viewMap));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ticket = listView.getItemAtPosition(i).toString();
                ticket= MapUtil.stringToMap(s.getViewMap()).get(ticket);

                Toast.makeText(getApplicationContext()," Before Sending TiketID:"+ticket,Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(), ViewTickets.class);
                intent.putExtra("ticketid", ticket);
                startActivity(intent);
            }
        });
    }
}
