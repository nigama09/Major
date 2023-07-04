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
import com.example.qrcodebasedbussticketbookingsystem.form.Stop;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.MapUtil;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ListStops extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stops);

        listView=(ListView) findViewById(R.id.StopList);

        final Session s=new Session(getApplicationContext());

        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String busid = savedInstanceState.getString("busid");

        final Map<String,String> viewMap=new HashMap<String,String>();

        DAO d=new DAO();
        d.getDBReference(Constants.STOPS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                    Stop stop=(Stop)snapshotNode.getValue(Stop.class);

                    viewMap.put("1-"+stop.getStopname(),stop.getStopid());
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

                String stop = listView.getItemAtPosition(i).toString();
                stop= MapUtil.stringToMap(s.getViewMap()).get(stop);

                Intent intent=new Intent(getApplicationContext(), ViewStops.class);
                intent.putExtra("stopid", stop);
                startActivity(intent);
            }
        });
    }
}
