package com.msn.julio_net.desafiobtg.utils;

public class Constantes {

    static final String PREF_ID = "desafio_btg";
    public static final String DATABASE_NAME = "app.sqlite";
    public static final String TABLE_MOVIE = "movies";
    public static final String TABLE_GENRE = "genres";
    public static final String URL_API_MOVIE = "https://api.themoviedb.org/3/movie/popular?api_key=e06c8959fc1bf1244017a16fc31481dd&language=pt-BR&page=%s";
    public static final String URL_API_GENRE = "https://api.themoviedb.org/3/genre/movie/list?api_key=e06c8959fc1bf1244017a16fc31481dd&language=pt-BR";
    public static final String URL_IMAGES = "http://image.tmdb.org/t/p/w92";
    public static final String LAST_PAGE_DOWNLOAD = "last_page_download";
    public static final String VIEW_PAGER_INDEX = "view_pager_index";
    public static final String INDEX_POSITION_MOVIE = "index_position_movie";
    public static final String TOP_POSITION_MOVIE = "top_position_movie";
    public static final String INDEX_POSITION_FAVORITE = "index_position_favorite";
    public static final String TOP_POSITION_FAVORITE = "top_position_favorite";
    public static final String MOVIE_ORDER = "movie_order";
    public static final String FAVORITE_ORDER = "favorite_order";
}
