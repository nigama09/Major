package com.example.qrcodebasedbussticketbookingsystem.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class Session {

    private SharedPreferences prefs;
    Context c;
    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        c=cntx;
    }

    public void setusername(String usename) {
        prefs.edit().putString("username", usename).commit();
    }

    public String getusername() {
        String usename = prefs.getString("username","");
        return usename;
    }

    public void setRole(String role) {
        prefs.edit().putString("role", role).commit();
    }

    public String getRole() {
        String role = prefs.getString("role","");
        return role;
    }

    public void setViewMap(String viewMap) {
        prefs.edit().putString("viewMap", viewMap).commit();
    }

    public String getViewMap() {
        String viewMap = prefs.getString("viewMap","");
        return viewMap;
    }

    public void loggingOut()
    {
        SharedPreferences p1=c.getSharedPreferences(getusername(),c.MODE_PRIVATE);
        p1.edit().remove(getusername()).commit();
    }
}
