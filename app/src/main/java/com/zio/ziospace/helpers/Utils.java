package com.zio.ziospace.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class Utils {

    Context context;


    public Utils(Context context) {
        this.context = context;
    }

    public static boolean stringExistsInArray(String[] array, String target) {
        for (String element : array) {
            if (element.equals(target)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkwifi() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (wificon != null && wificon.isConnected());
    }

    public static boolean checknetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wificon = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobcon = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wificon != null && wificon.isConnected()) || (mobcon != null && mobcon.isConnected());

    }

    public static void lost_net_page(Context context) {
        //context.startActivity(new Intent(context, LostNet.class));
    }

    public static void show_connect_net_dialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet")
                .setMessage("please connect to the internet to proceed further ")
                .setCancelable(false)
                .setPositiveButton("connect now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

}
