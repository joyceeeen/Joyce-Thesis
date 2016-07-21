package com.thesis.volunteam2.Manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.thesis.volunteam2.LoginActivity;

import java.util.HashMap;

/**
 * Created by Jeysown on 7/20/2016.
 */

public class SessionManager extends AppCompatActivity{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "myPrefs";
    public static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_PASS = "pass";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERID = "userID";

    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String pass, String userID){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASS,pass);
        editor.putString(KEY_USERID,userID);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            this.finish();
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        user.put(KEY_PASS,pref.getString(KEY_PASS,null));
        return user;
    }
    public void logOutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN,false);
    }

    public String getUSERID(){
       return pref.getString(KEY_USERID,null);
    }

}
