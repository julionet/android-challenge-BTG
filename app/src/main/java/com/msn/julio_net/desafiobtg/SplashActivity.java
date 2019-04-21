package com.msn.julio_net.desafiobtg;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.msn.julio_net.desafiobtg.controllers.GenreController;
import com.msn.julio_net.desafiobtg.controllers.MovieController;
import com.msn.julio_net.desafiobtg.models.Movie;
import com.msn.julio_net.desafiobtg.models.Genre;
import com.msn.julio_net.desafiobtg.models.RetornoGenre;
import com.msn.julio_net.desafiobtg.models.RetornoMovie;
import com.msn.julio_net.desafiobtg.utils.Constantes;
import com.msn.julio_net.desafiobtg.utils.NetworkUtils;
import com.msn.julio_net.desafiobtg.utils.Preferences;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    TextView textViewBuscando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textViewBuscando = findViewById(R.id.text_view_buscando);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Preferences.setInteger(getBaseContext(), Constantes.INDEX_POSITION_MOVIE, -1);
                Preferences.setInteger(getBaseContext(), Constantes.TOP_POSITION_MOVIE, -1);
                Preferences.setInteger(getBaseContext(), Constantes.INDEX_POSITION_FAVORITE, -1);
                Preferences.setInteger(getBaseContext(), Constantes.TOP_POSITION_FAVORITE, -1);
                Preferences.setInteger(getBaseContext(), Constantes.VIEW_PAGER_INDEX, 0);
                if (Preferences.getInteger(getBaseContext(), Constantes.LAST_PAGE_DOWNLOAD) == 0) {
                    Preferences.setInteger(getBaseContext(), Constantes.LAST_PAGE_DOWNLOAD, 0);
                }
                if (Preferences.getString(getBaseContext(), Constantes.MOVIE_ORDER).equals("")) {
                    Preferences.setString(getBaseContext(), Constantes.MOVIE_ORDER, "");
                }
                if (Preferences.getString(getBaseContext(), Constantes.FAVORITE_ORDER).equals("")) {
                    Preferences.setString(getBaseContext(), Constantes.FAVORITE_ORDER, "");
                }

                if (NetworkUtils.isNetworkAvailable(getBaseContext())) {
                    downloadInformacoes();
                } else {
                    loadActivity();
                }
            }
        }, 2000);
    }

    private void loadActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void downloadInformacoes() {
        if (GenreController.contar(getBaseContext()) == 0) {
            GetGenres();
        } else {
            if (MovieController.contar(getBaseContext()) == 0) {
                GetMovies();
            } else {
                loadActivity();
            }
        }
    }

    private void GetGenres() {
        String url = Constantes.URL_API_GENRE;
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                loadActivity();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    RetornoGenre retorno = new Gson().fromJson(json, RetornoGenre.class);

                    for (Genre genre: retorno.getGenres()) {
                        if (GenreController.selecionar(getBaseContext(), genre.getId()) == null) {
                            GenreController.inserir(getBaseContext(), genre);
                        }
                    }
                    GetMovies();
                }
                else
                    loadActivity();
            }
        });
    }

    private void GetMovies() {
        SplashActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewBuscando.setVisibility(View.VISIBLE);
            }
        });

        String url = String.format(Constantes.URL_API_MOVIE, 1);
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                loadActivity();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    RetornoMovie retorno = new Gson().fromJson(json, RetornoMovie.class);
                    Preferences.setInteger(getBaseContext(), Constantes.LAST_PAGE_DOWNLOAD, 1);

                    if (retorno.getResults().length != 0) {
                        for (Movie movie : retorno.getResults()) {
                            if (MovieController.selecionar(getBaseContext(), movie.getId()) == null) {
                                movie.setFavorite(false);
                                movie.setGenres(GenreController.getNames(getBaseContext(), movie.getGenre_ids()));
                                try {
                                    MovieController.inserir(getBaseContext(), movie);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                }
                loadActivity();
            }
        });
    }
}
