package com.msn.julio_net.desafiobtg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static void setString(Context context, String chave, String valor) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        if (pref != null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(chave, valor);
            editor.apply();
        }
    }

    public static String getString(Context context, String chave) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        if (pref != null) {
            return pref.getString(chave, "");
        } else {
            return "";
        }
    }

    public static void setBoolean(Context context, String chave, Boolean valor) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chave, valor);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String chave) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        return pref != null && pref.getBoolean(chave, false);
    }

    public static void setInteger(Context context, String chave, int valor) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(chave, valor);
        editor.apply();
    }

    public static int getInteger(Context context, String chave) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        return pref.getInt(chave, 0);
    }

    public static void setLong(Context context, String chave, long valor) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(chave, valor);
        editor.apply();
    }

    public static long getLong(Context context, String chave) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.PREF_ID, Activity.MODE_PRIVATE);
        return pref.getLong(chave, 0);
    }
}
