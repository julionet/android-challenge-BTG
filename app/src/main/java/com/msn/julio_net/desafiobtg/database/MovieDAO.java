package com.msn.julio_net.desafiobtg.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.msn.julio_net.desafiobtg.models.Movie;
import com.msn.julio_net.desafiobtg.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private SQLiteDatabase db;

    public MovieDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(Movie entity) {
        try {
            ContentValues values = new ContentValues();
            values.put("id", entity.getId());
            values.put("title", entity.getTitle());
            values.put("overview", entity.getOverview());
            values.put("releasedate", entity.getRelease_date());
            values.put("voteaverage", entity.getVote_average());
            values.put("posterpath", entity.getPoster_path());
            values.put("genres", entity.getGenres());
            values.put("favorite", entity.getFavorite());
            values.put("poster", entity.getPoster());
            db.insert(Constantes.TABLE_MOVIE, "", values);
        } finally {
            db.close();
        }
    }

    public void update(Movie entity) {
        try {
            ContentValues values = new ContentValues();
            values.put("id", entity.getId());
            values.put("title", entity.getTitle());
            values.put("overview", entity.getOverview());
            values.put("releasedate", entity.getRelease_date());
            values.put("voteaverage", entity.getVote_average());
            values.put("posterpath", entity.getPoster_path());
            values.put("genres", entity.getGenres());
            values.put("favorite", entity.getFavorite());
            values.put("poster", entity.getPoster());
            db.update(Constantes.TABLE_MOVIE, values, "id = ?", new String[]{String.valueOf(entity.getId())});
        } finally {
            db.close();
        }
    }

    public void delete(int id) {
        try {
            db.delete(Constantes.TABLE_MOVIE, "id = ?", new String[] { String.valueOf(id) });
        } finally {
            db.close();
        }
    }

    public void deleteAll() {
        try {
            db.delete(Constantes.TABLE_MOVIE, null, null);
        } finally {
            db.close();
        }
    }

    public List<Movie> getAll(String ordem) {
        try {
            Cursor c = db.query(Constantes.TABLE_MOVIE, null, null, null, null, null, ordem.equals("") ? null : ordem);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public Movie getById(int id) {
        try {
            Cursor c = db.query(Constantes.TABLE_MOVIE, null, "id = " + String.valueOf(id), null, null, null, null);
            List<Movie> registros = toList(c);
            if (registros.size() != 0) {
                return toList(c).get(0);
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public List<Movie> getFavorites(String ordem) {
        try {
            Cursor c = db.query(Constantes.TABLE_MOVIE, null, "favorite = 1", null, null, null, ordem.equals("") ? null : ordem);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public long count() {
        try {
            return DatabaseUtils.queryNumEntries(db, Constantes.TABLE_MOVIE);
        } finally {
            db.close();
        }
    }

    public void dropTable() {
        db.execSQL("drop table if exists " + Constantes.TABLE_MOVIE + ";");
    }

    private List<Movie> toList(Cursor c) {
        List<Movie> registros = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Movie registro = new Movie();
                registro.setId(c.getInt(c.getColumnIndex("id")));
                registro.setTitle(c.getString(c.getColumnIndex("title")));
                registro.setOverview(c.getString(c.getColumnIndex("overview")));
                registro.setRelease_date(c.getString(c.getColumnIndex("releasedate")));
                registro.setVote_average(c.getFloat(c.getColumnIndex("voteaverage")));
                registro.setPoster_path(c.getString(c.getColumnIndex("posterpath")));
                registro.setGenres(c.getString(c.getColumnIndex("genres")));
                registro.setFavorite(c.getInt(c.getColumnIndex("favorite")) == 1);
                registro.setPoster(c.getBlob(c.getColumnIndex("poster")));
                registros.add(registro);
            } while (c.moveToNext());
        }
        return registros;
    }
}
