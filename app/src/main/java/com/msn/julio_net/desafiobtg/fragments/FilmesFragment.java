package com.msn.julio_net.desafiobtg.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.msn.julio_net.desafiobtg.DetalheActivity;
import com.msn.julio_net.desafiobtg.R;
import com.msn.julio_net.desafiobtg.adapters.FilmesAdapter;
import com.msn.julio_net.desafiobtg.controllers.GenreController;
import com.msn.julio_net.desafiobtg.controllers.MovieController;
import com.msn.julio_net.desafiobtg.models.Movie;
import com.msn.julio_net.desafiobtg.models.RetornoMovie;
import com.msn.julio_net.desafiobtg.utils.Constantes;
import com.msn.julio_net.desafiobtg.utils.KeyboardUtils;
import com.msn.julio_net.desafiobtg.utils.Preferences;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FilmesFragment extends Fragment {

    Context _context;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayoutFilmes;
    List<Movie> _movies;

    LinearLayoutManager linearLayoutManager;
    public FilmesAdapter _adapter;
    FavoritosFragment _fragment;

    MenuItem _menuItemSearch;

    public FilmesFragment() {

    }

    public void setFavoritosFragment(FavoritosFragment fragment) {
        this._fragment = fragment;
    }

    public void set_pesquisando(boolean pesquisando) {
        _adapter.setSearched(pesquisando);
    }

    public void set_menuItemSearch(MenuItem menuItemSearch) {
        this._menuItemSearch = menuItemSearch;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filmes, container, false);

        swipeRefreshLayoutFilmes = view.findViewById(R.id.swipe_filme);
        swipeRefreshLayoutFilmes.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int page = Preferences.getInteger(_context, Constantes.LAST_PAGE_DOWNLOAD);
                GetMovies(page + 1);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerview_filmes);
        linearLayoutManager = new LinearLayoutManager(requireActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);

        _movies = MovieController.selecionarTodos(_context, Preferences.getString(_context, Constantes.MOVIE_ORDER));
        _adapter = new FilmesAdapter(_context, _movies, true, onClickItem(), _fragment, recyclerView);
        _adapter.setOnLoadMoreListener(new FilmesAdapter.FilmeOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshLayoutFilmes.setRefreshing(true);
                int page = Preferences.getInteger(_context, Constantes.LAST_PAGE_DOWNLOAD);
                GetMovies(page + 1);
            }
        });

        recyclerView.setAdapter(_adapter);

        if (Preferences.getInteger(_context, Constantes.INDEX_POSITION_MOVIE) != -1) {
            int index = Preferences.getInteger(_context, Constantes.INDEX_POSITION_MOVIE);
            int top = Preferences.getInteger(_context, Constantes.TOP_POSITION_MOVIE);
            linearLayoutManager.scrollToPositionWithOffset(index, top);
        }

        return view;
    }

    public void updateAdapterRecyclerView() {
        _movies = MovieController.selecionarTodos(_context, Preferences.getString(_context, Constantes.MOVIE_ORDER));
        _adapter.setList(_movies);
        _adapter.notifyDataSetChanged();
        if (_menuItemSearch != null)
            _menuItemSearch.collapseActionView();
    }

    private FilmesAdapter.FilmeOnClickListener onClickItem() {
        return new FilmesAdapter.FilmeOnClickListener() {
            @Override
            public void onClickItem(View view, int i) {
                KeyboardUtils.disableDoubleClicks(view);
                Intent intent = new Intent(requireActivity(), DetalheActivity.class);
                intent.putExtra("movie", _adapter.getList().get(i));
                Preferences.setInteger(_context, Constantes.VIEW_PAGER_INDEX, 0);
                Preferences.setInteger(_context, Constantes.INDEX_POSITION_MOVIE, linearLayoutManager.findFirstVisibleItemPosition());
                Preferences.setInteger(_context, Constantes.TOP_POSITION_MOVIE,
                        (recyclerView.getChildAt(0) == null) ? 0 :
                                (recyclerView.getChildAt(0).getTop() - recyclerView.getPaddingTop()));
                startActivity(intent);
            }
        };
    }

    private void GetMovies(final int page) {
        String url = String.format(Constantes.URL_API_MOVIE, page);
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

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    RetornoMovie retorno = new Gson().fromJson(json, RetornoMovie.class);
                    Preferences.setInteger(_context, Constantes.LAST_PAGE_DOWNLOAD, page);

                    if (retorno.getResults().length != 0 ) {
                        for (Movie movie : retorno.getResults()) {
                            if (MovieController.selecionar(_context, movie.getId()) == null) {
                                movie.setFavorite(false);
                                movie.setGenres(GenreController.getNames(_context, movie.getGenre_ids()));
                                try {
                                    MovieController.inserir(_context, movie);
                                } catch (Exception ignored) { }
                            }
                        }
                    }
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapterRecyclerView();
                        swipeRefreshLayoutFilmes.setRefreshing(false);
                        _adapter.setLoaded(false);
                        _adapter.setSearched(false);
                    }
                });
            }
        });
    }
}
