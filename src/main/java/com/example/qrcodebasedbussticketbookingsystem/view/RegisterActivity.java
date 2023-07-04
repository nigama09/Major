package com.example.qrcodebasedbussticketbookingsystem.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

import com.example.qrcodebasedbussticketbookingsystem.R;
import com.example.qrcodebasedbussticketbookingsystem.dao.DAO;
import com.example.qrcodebasedbussticketbookingsystem.form.User;
import com.example.qrcodebasedbussticketbookingsystem.util.Constants;

public class RegisterActivity extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5,e6,e7;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        e1=(EditText)findViewById(R.id.registerPhone);
        e2=(EditText)findViewById(R.id.registerPassword);
        e3=(EditText)findViewById(R.id.registerConPass);
        e4=(EditText)findViewById(R.id.registerEmail);
        e5=(EditText)findViewById(R.id.registerMobile);
        e6=(EditText)findViewById(R.id.registerName);
        e7=(EditText)findViewById(R.id.registerAddress);

        b1=(Button)findViewById(R.id.registerButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username=e1.getText().toString();
                String password=e2.getText().toString();
                String conformPassword=e3.getText().toString();
                String email=e4.getText().toString();
                String mobile=e5.getText().toString();
                String name=e6.getText().toString();
                String address=e7.getText().toString();

                if(username==null|| password==null|| conformPassword==null|| email==null|| mobile==null|| name==null|| address==null)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Data",Toast.LENGTH_SHORT).show();
                }
                else if(mobile.length()<10|| mobile.length()>12)
                {
                    Toast.makeText(getApplicationContext(),"Invalid Mobile",Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(conformPassword))
                {
                    Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                }
                else if(!isValid(email))
                {
                    Toast.makeText(getApplicationContext(),"Plase Enter Valid Email",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User user=new User();

                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setMobile(mobile);
                    user.setName(name);
                    user.setAddress(address);

                    DAO dao=new DAO();

                    try
                    {
                        dao.addObject(Constants.USER_DB,user,user.getUsername());

                        Toast.makeText(getApplicationContext(),"Register Success",Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(getApplicationContext(),"Register Error",Toast.LENGTH_SHORT).show();
                        Log.v("User Registration Ex", ex.toString());
                        ex.printStackTrace();
                    }

                }
            }
        });
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
