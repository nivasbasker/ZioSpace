package com.zio.ziospace.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.zio.ziospace.data.ModelUser;

import java.util.HashMap;

public class LoginManager {
    private static final String KEY_LOGGEDIN = "Login", KEY_USERNAME = "Username", KEY_FULLNAME = "FullName",
            KEY_EMAIL = "Email", KEY_PHONE_NO = "PhoneNo", KEY_PASSWORD = "Password", KEY_SESSION = "LoginSession";

    private final SharedPreferences LoginSession;
    private SharedPreferences.Editor editor;
    private final Context context;

    public LoginManager(Context _context) {

        context = _context;
        LoginSession = _context.getSharedPreferences(KEY_SESSION, Context.MODE_PRIVATE);

    }

    public boolean loginUser(ModelUser user) {

        editor = LoginSession.edit();
        editor.putBoolean(KEY_LOGGEDIN, true);
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_FULLNAME, user.getFullname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_PHONE_NO, user.getPhoneno());

        return editor.commit();

    }

    public HashMap<String, String> getUserData() {

        HashMap<String, String> userdata = new HashMap<String, String>();

        userdata.put(KEY_USERNAME, LoginSession.getString(KEY_USERNAME, null));
        userdata.put(KEY_FULLNAME, LoginSession.getString(KEY_FULLNAME, null));
        userdata.put(KEY_EMAIL, LoginSession.getString(KEY_EMAIL, null));
        userdata.put(KEY_PHONE_NO, LoginSession.getString(KEY_PHONE_NO, null));
        userdata.put(KEY_PASSWORD, LoginSession.getString(KEY_PASSWORD, null));

        return userdata;
    }

    public static String getUserName(Context context1) {
        return context1.getSharedPreferences(KEY_SESSION, Context.MODE_PRIVATE).getString(KEY_USERNAME, null);
    }

    public boolean isLoggedIn() {
        return LoginSession.getBoolean(KEY_LOGGEDIN, false);
    }

    public boolean logoutUser() {

        Intent logoutIntent = new Intent(Constants.LOGOUT_ACTION);
        context.sendBroadcast(logoutIntent);
        return LoginSession.edit().clear().commit();

    }

}
