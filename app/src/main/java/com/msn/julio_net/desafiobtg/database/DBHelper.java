package com.msn.julio_net.desafiobtg.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.msn.julio_net.desafiobtg.utils.Constantes;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, Constantes.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + Constantes.TABLE_MOVIE +
                " (id integer primary key, title text, overview text, releasedate text, voteaverage float, " +
                "  posterpath text, genres text, favorite int, poster blob);");

        db.execSQL("create table if not exists " + Constantes.TABLE_GENRE +
                " (id integer primary key, name text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
