package com.example.qrcodebasedbussticketbookingsystem.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.Bus;
import com.example.qrcodebasedbussticketbookingsystem.form.Stop;
import com.example.qrcodebasedbussticketbookingsystem.form.Ticket;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;
import com.example.qrcodebasedbussticketbookingsystem.util.Session;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BookTicket extends AppCompatActivity {

    private Spinner stop;
    private String stopString;

    private Spinner seat;
    private String seatString;

    RadioGroup usertype;
    RadioButton r1;
    private String usertypeString;

    Button bookTicket;
    Button cancelTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);

        usertype=(RadioGroup)findViewById(R.id.radiogroupusertype);

        stop=(Spinner)findViewById(R.id.spinnerStop) ;
        seat=(Spinner)findViewById(R.id.spinnerseatnumber) ;

        bookTicket=(Button)findViewById(R.id.bookticketsubmit);
        cancelTicket=(Button)findViewById(R.id.bookticketcancel);


        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String busid = savedInstanceState.getString("busid");
        final String tdate = savedInstanceState.getString("tdate");
        final Session s=new Session(getApplicationContext());

        DAO d=new DAO();
        d.getDBReference(Constants.BUSSES_DB).child(busid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Bus bus=dataSnapshot.getValue(Bus.class);

                if(bus!=null)
                {
                    final List<String> stops=new ArrayList<>();

                    DAO d=new DAO();
                    d.getDBReference(Constants.STOPS_DB).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                                Stop stop=(Stop)snapshotNode.getValue(Stop.class);

                                if(stop.getBusno().equals(busid))
                                {
                                    stops.add(stop.getStopname());
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(stop.getContext(),
                                    android.R.layout.simple_list_item_1, (stops.toArray(new String[stops.size()])));

                            stop.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    d.getDBReference(Constants.Ticket_DB).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final List<String> seats=new ArrayList<String>();

                            for (int i=1;i<Integer.parseInt(bus.getNoOfSeats());i++)
                            {
                                seats.add(i+"");
                            }

                            for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {

                                Ticket ticket=(Ticket) snapshotNode.getValue(Ticket.class);

                                Log.v("in for "+ticket.getDt()," input date:"+tdate);

                                if(tdate.equals(ticket.getDt()) && ticket.getBusno().equals(busid))
                                {
                                    Log.v("in for "+ticket.getDt()," input date:"+tdate +" matched in if");
                                    seats.remove(ticket.getSeatno());
                                }
                                else
                                {
                                    Log.v("in for "+ticket.getDt()," input date:"+tdate +" not matched in else");
                                }
                            }

                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(seat.getContext(),
                                    android.R.layout.simple_list_item_1, (seats.toArray(new String[seats.size()])));

                            seat.setAdapter(adapter1);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Spinner click listener
        stop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stopString= adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Spinner click listener
        seat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seatString= adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // spinner setting end
        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DAO dao=new DAO();

                int utype=usertype.getCheckedRadioButtonId();
                r1=(RadioButton)findViewById(utype);
                usertypeString=r1.getText().toString();

                Ticket ticket=new Ticket();
                ticket.setTicketId(dao.getUnicKey(Constants.STOPS_DB));
                ticket.setBusno(busid);
                ticket.setAudiltorchild(usertypeString);
                ticket.setDt(tdate);
                ticket.setSeatno(seatString);
                ticket.setStop(stopString);
                ticket.setUserid(s.getusername());

                dao.addObject(Constants.Ticket_DB,ticket,ticket.getTicketId());

                Toast.makeText(getApplicationContext(),"Your Ticket is Conformed",Toast.LENGTH_SHORT).show();

                final String ticketInfo="Bus NO:"+busid+" \n Adult/Child:"+usertypeString+"\n Seat No:" +seatString+"\n Destination :" +stopString+"\n Date:" +tdate;

                Runnable r=new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Bitmap bitmap = TextToImageEncode(ticketInfo);
                            saveImage(bitmap);  //give read write permission
                            Toast.makeText(BookTicket.this, "QRCode is Saved", Toast.LENGTH_SHORT).show();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                r.run();

                Intent i=new Intent(getApplicationContext(),UserHome.class);
                startActivity(i);

            }
        });

        cancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), UserHome.class);
                startActivity(i);
            }
        });
    }

    public void saveImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        MediaStore.Images.Media.insertImage(getContentResolver(), myBitmap, "qrimae" , "Bus Ticket");

    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {

            bitMatrix = new MultiFormatWriter().encode(Value,BarcodeFormat.DATA_MATRIX.QR_CODE,500, 500, null);

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack):getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
