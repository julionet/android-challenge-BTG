package com.msn.julio_net.desafiobtg;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msn.julio_net.desafiobtg.controllers.MovieController;
import com.msn.julio_net.desafiobtg.models.Movie;
import com.msn.julio_net.desafiobtg.utils.ConvertHelper;

public class DetalheActivity extends AppCompatActivity {

    Movie _movie;

    ImageView imageViewPoster;
    TextView textViewTitulo;
    TextView textViewAno;
    TextView textViewNota;
    TextView textViewGenres;
    TextView textViewSinopse;
    ImageView imageViewFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        _movie = (Movie) getIntent().getSerializableExtra("movie");

        imageViewPoster = findViewById(R.id.image_view_poster_detalhe);
        textViewTitulo = findViewById(R.id.text_view_nome_detalhe);
        textViewAno = findViewById(R.id.text_view_ano_detalhe);
        textViewNota = findViewById(R.id.text_view_nota_detalhe);
        textViewGenres = findViewById(R.id.text_view_genero_detalhe);
        textViewSinopse = findViewById(R.id.text_view_sinopse_detalhe);
        imageViewFavorite = findViewById(R.id.image_view_favorito);

        imageViewPoster.setImageBitmap(ConvertHelper.ByteArrayToBitmap(_movie.getPoster()));
        textViewTitulo.setText(_movie.getTitle());
        textViewAno.setText(String.format("(%s)", _movie.getRelease_date().substring(0, 4)));
        textViewNota.setText(String.format("%d%%", (int)(_movie.getVote_average()*10)));
        textViewGenres.setText(_movie.getGenres());
        textViewSinopse.setText(_movie.getOverview());
        if (_movie.getFavorite()) {
            imageViewFavorite.setImageResource(R.drawable.ic_action_favorite);
        } else {
            imageViewFavorite.setImageResource(R.drawable.ic_action_unfavorite);
        }

        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _movie.setFavorite(!_movie.getFavorite());
                MovieController.alterar(getBaseContext(), _movie);

                if (_movie.getFavorite()) {
                    imageViewFavorite.setImageResource(R.drawable.ic_action_favorite);
                } else {
                    imageViewFavorite.setImageResource(R.drawable.ic_action_unfavorite);
                }
            }
        });
    }
}
