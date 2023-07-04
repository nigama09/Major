package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Bus;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.MapUtil;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListBus extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bus);

        listView=(ListView) findViewById(R.id.BusList);
        final Session s=new Session(getApplicationContext());

        final Map<String,String> viewMap=new HashMap<String,String>();
        DAO d=new DAO();



        if(s.getRole().equals("user")) {
            Intent i = getIntent();
            savedInstanceState = i.getExtras();

            final String source = savedInstanceState.getString("source");
            final String destination = savedInstanceState.getString("destination");
            final String tdate = savedInstanceState.getString("tdate");

            d.getDBReference(Constants.BUSSES_DB).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {

                        Bus bus = (Bus) snapshotNode.getValue(Bus.class);

                        if (bus.getSource().contains(source) && bus.getDest().contains(destination)) {
                            viewMap.put("1-" + bus.getBusName(), bus.getBusNo()+"@"+tdate);
                        }
                    }

                    ArrayList<String> al = new ArrayList<String>(viewMap.keySet());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));
                    listView.setAdapter(adapter);

                    Session s = new Session(getApplicationContext());
                    s.setViewMap(MapUtil.mapToString(viewMap));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {

            d.getDBReference(Constants.BUSSES_DB).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {

                        Bus bus = (Bus) snapshotNode.getValue(Bus.class);

                        viewMap.put("1-" + bus.getBusName(), bus.getBusNo());
                    }

                    ArrayList<String> al = new ArrayList<String>(viewMap.keySet());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));
                    listView.setAdapter(adapter);

                    Session s = new Session(getApplicationContext());
                    s.setViewMap(MapUtil.mapToString(viewMap));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String bus = listView.getItemAtPosition(i).toString();
                bus= MapUtil.stringToMap(s.getViewMap()).get(bus);

                Intent intent=new Intent(getApplicationContext(),ViewBus.class);
                intent.putExtra("busid", bus);
                startActivity(intent);
            }
        });
    }
}
