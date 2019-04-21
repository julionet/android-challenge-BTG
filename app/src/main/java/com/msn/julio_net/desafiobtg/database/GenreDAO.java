package com.msn.julio_net.desafiobtg.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.msn.julio_net.desafiobtg.models.Genre;
import com.msn.julio_net.desafiobtg.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    private SQLiteDatabase db;

    public GenreDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(Genre entity) {
        try {
            ContentValues values = new ContentValues();
            values.put("id", entity.getId());
            values.put("name", entity.getName());
            db.insert(Constantes.TABLE_GENRE, "", values);
        } finally {
            db.close();
        }
    }

    public void update(Genre entity) {
        try {
            ContentValues values = new ContentValues();
            values.put("id", entity.getId());
            values.put("name", entity.getName());
            db.update(Constantes.TABLE_GENRE, values, "id = ?", new String[]{String.valueOf(entity.getId())});
        } finally {
            db.close();
        }
    }

    public void delete(int id) {
        try {
            db.delete(Constantes.TABLE_GENRE, "id = ?", new String[] { String.valueOf(id) });
        } finally {
            db.close();
        }
    }

    public void deleteAll() {
        try {
            db.delete(Constantes.TABLE_GENRE, null, null);
        } finally {
            db.close();
        }
    }

    public List<Genre> getAll() {
        try {
            Cursor c = db.query(Constantes.TABLE_GENRE, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public Genre getById(int id) {
        try {
            Cursor c = db.query(Constantes.TABLE_GENRE, null, "id = " + String.valueOf(id), null, null, null, null);
            List<Genre> registros = toList(c);
            if (registros.size() != 0) {
                return toList(c).get(0);
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public long count() {
        try {
            return DatabaseUtils.queryNumEntries(db, Constantes.TABLE_GENRE);
        } finally {
            db.close();
        }
    }

    public void dropTable() {
        db.execSQL("drop table if exists " + Constantes.TABLE_GENRE + ";");
    }

    private List<Genre> toList(Cursor c) {
        List<Genre> registros = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Genre registro = new Genre();
                registro.setId(c.getInt(c.getColumnIndex("id")));
                registro.setName(c.getString(c.getColumnIndex("name")));
                registros.add(registro);
            } while (c.moveToNext());
        }
        return registros;
    }
}
