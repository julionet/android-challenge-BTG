package com.msn.julio_net.desafiobtg.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.msn.julio_net.desafiobtg.database.DBHelper;
import com.msn.julio_net.desafiobtg.database.GenreDAO;
import com.msn.julio_net.desafiobtg.database.MovieDAO;
import com.msn.julio_net.desafiobtg.models.Genre;
import com.msn.julio_net.desafiobtg.models.Movie;

public class GenreController {

    public static void inserir(Context context, Genre entity) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getWritableDatabase()) {
            try {
                GenreDAO dao = new GenreDAO(database);
                dao.insert(entity);
            } finally {
                database.close();
            }
            helper.close();
        }
    }

    public static Genre selecionar(Context context, int id) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getReadableDatabase()) {
            try {
                GenreDAO dao = new GenreDAO(database);
                return dao.getById(id);
            } finally {
                database.close();
            }
        }  finally {
            helper.close();
        }
    }

    public static long contar(Context context) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getReadableDatabase()) {
            try {
                GenreDAO dao = new GenreDAO(database);
                return dao.count();
            } finally {
                database.close();
            }
        }  finally {
            helper.close();
        }
    }

    public static String getNames(Context context, int[] genres) {
        String names = "";
        for (int id: genres) {
            Genre genre = selecionar(context, id);
            if (genre != null) {
                names = names.concat(names.equals("") ? genre.getName() : ", " + genre.getName());
            }
        }
        return names;
    }
}
