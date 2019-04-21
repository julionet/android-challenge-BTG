package com.msn.julio_net.desafiobtg.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.msn.julio_net.desafiobtg.database.DBHelper;
import com.msn.julio_net.desafiobtg.database.GenreDAO;
import com.msn.julio_net.desafiobtg.database.MovieDAO;
import com.msn.julio_net.desafiobtg.models.Genre;
import com.msn.julio_net.desafiobtg.models.Movie;

import java.util.List;

public class MovieController {

    public static void inserir(Context context, Movie entity) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getWritableDatabase()) {
            try {
                MovieDAO dao = new MovieDAO(database);
                dao.insert(entity);
            } finally {
                database.close();
            }
            helper.close();
        }
    }

    public static void alterar(Context context, Movie entity) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getWritableDatabase()) {
            try {
                MovieDAO dao = new MovieDAO(database);
                dao.update(entity);
            } finally {
                database.close();
            }
            helper.close();
        }
    }

    public static List<Movie> selecionarTodos(Context context, String ordem) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getReadableDatabase()) {
            try {
                MovieDAO dao = new MovieDAO(database);
                return dao.getAll(ordem);
            } finally {
                database.close();
            }
        }  finally {
            helper.close();
        }
    }

    public static List<Movie> selecionarFavoritos(Context context, String ordem) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getReadableDatabase()) {
            try {
                MovieDAO dao = new MovieDAO(database);
                return dao.getFavorites(ordem);
            } finally {
                database.close();
            }
        }  finally {
            helper.close();
        }
    }

    public static Movie selecionar(Context context, int id) {
        DBHelper helper = new DBHelper(context);
        try (SQLiteDatabase database = helper.getReadableDatabase()) {
            try {
                MovieDAO dao = new MovieDAO(database);
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
                MovieDAO dao = new MovieDAO(database);
                return dao.count();
            } finally {
                database.close();
            }
        }  finally {
            helper.close();
        }
    }
}
