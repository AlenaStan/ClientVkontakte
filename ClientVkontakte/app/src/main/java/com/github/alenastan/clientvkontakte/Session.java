package com.github.alenastan.clientvkontakte;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.alenastan.clientvkontakte.auth.EncrManager;

/**
 * Created by lena on 30.01.2015.
 */
public class Session {

    public static final String ACCESS_TOKEN = "access_token";
    private static Session session;
    protected static String mToken;

    public Session () {

    }

    public  String getToken(){
        return mToken;
    }


    protected  void saveTokenToSharedPreferences(String s,Context context) {
        mToken = s;
        String encToken = null;
        try {
            encToken = EncrManager.encrypt(context,s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(ACCESS_TOKEN, encToken);
        edit.commit();

    }

    public static String getTokenFromSharedPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sToken = prefs.getString(ACCESS_TOKEN,"");
        try {
            sToken = EncrManager.decrypt(context, sToken);
            return sToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}