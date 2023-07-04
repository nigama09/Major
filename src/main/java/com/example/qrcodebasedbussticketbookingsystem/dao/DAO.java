package com.example.qrcodebasedbussticketbookingsystem.dao;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.firebase.storage.StorageReference;
import com.example.qrcodebasedbussticketbookingsystem.form.Bus;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;

public class DAO
{
        public static DatabaseReference getDBReference(String dbName)
        {
            return GetFireBaseConnection.getConnection(dbName);
        }

        public static String getUnicKey(String dbName)
        {
            return getDBReference(dbName).push().getKey();
        }

        public static StorageReference getStorageReference() {
            return GetFireBaseConnection.getStorageReference();
        }


    public int addObject(String dbName,Object obj,String key) {

            int result=0;

            try {

                getDBReference(dbName).child(key).setValue(obj);

                result=1;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return result;
        }


        public void setDataToAdapterList(final View view, final Class c, final String dbname, final String userType) {

            final ArrayList<String> al=new ArrayList<String>();

            getDBReference(dbname).addValueEventListener(new ValueEventListener() {
                int i=1;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                       String id=snapshotNode.getKey();

                        Object object=snapshotNode.getValue(c);

                        if(dbname.equals(Constants.BUSSES_DB)) {

                            Bus bus=(Bus)object;

                            al.add(bus.getBusName()+"-"+bus.getBusNo());
                        }
                    }

                    if(view instanceof ListView) {

                        Log.v("in list view setting ",al.toString());

                        final ListView myView=(ListView)view;

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(myView.getContext(),
                                android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

                        myView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        }

        public int deleteObject(String dbName, String key) {

            int result=0;

            try {

                getDBReference(dbName).child(key).removeValue();

                result=1;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return 0;
        }
    }


