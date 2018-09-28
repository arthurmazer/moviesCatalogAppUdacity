package com.mazerapp.moviecatalogapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * Created by arthu on 13/09/2018.
 */

public class Util{

    public Util(){}

    public static void showDialog(Context context, String title, String text){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(text)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static String dateFormat(String strDate) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        return sdf2.format(sdf1.parse(strDate));
    }

}
