package com.msn.julio_net.desafiobtg.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msn.julio_net.desafiobtg.DetalheActivity;
import com.msn.julio_net.desafiobtg.R;
import com.msn.julio_net.desafiobtg.adapters.FilmesAdapter;
import com.msn.julio_net.desafiobtg.controllers.MovieController;
import com.msn.julio_net.desafiobtg.models.Movie;
import com.msn.julio_net.desafiobtg.utils.Constantes;
import com.msn.julio_net.desafiobtg.utils.KeyboardUtils;
import com.msn.julio_net.desafiobtg.utils.Preferences;

import java.util.List;

public class FavoritosFragment extends Fragment {

    Context _context;
    RecyclerView recyclerView;
    List<Movie> _movies;

    LinearLayoutManager linearLayoutManager;
    public FilmesAdapter _adapter;

    public FavoritosFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_favoritos);
        linearLayoutManager = new LinearLayoutManager(requireActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);

        _movies = MovieController.selecionarFavoritos(_context, Preferences.getString(_context, Constantes.FAVORITE_ORDER));
        _adapter = new FilmesAdapter(_context, _movies, false, onClickItem(), null, recyclerView);

        recyclerView.setAdapter(_adapter);

        if (Preferences.getInteger(_context, Constantes.INDEX_POSITION_FAVORITE) != -1) {
            int index = Preferences.getInteger(_context, Constantes.INDEX_POSITION_FAVORITE);
            int top = Preferences.getInteger(_context, Constantes.TOP_POSITION_FAVORITE);
            linearLayoutManager.scrollToPositionWithOffset(index, top);
        }

        return view;
    }

    public void updateAdapterRecyclerView() {
        _movies = MovieController.selecionarFavoritos(_context, Preferences.getString(_context, Constantes.FAVORITE_ORDER));
        _adapter.setList(_movies);
        _adapter.notifyDataSetChanged();
    }

    private FilmesAdapter.FilmeOnClickListener onClickItem() {
        return new FilmesAdapter.FilmeOnClickListener() {
            @Override
            public void onClickItem(View view, int i) {
                KeyboardUtils.disableDoubleClicks(view);
                Intent intent = new Intent(requireActivity(), DetalheActivity.class);
                intent.putExtra("movie", _adapter.getList().get(i));
                Preferences.setInteger(_context, Constantes.VIEW_PAGER_INDEX, 1);
                Preferences.setInteger(_context, Constantes.INDEX_POSITION_FAVORITE, linearLayoutManager.findFirstVisibleItemPosition());
                Preferences.setInteger(_context, Constantes.TOP_POSITION_FAVORITE,
                        (recyclerView.getChildAt(0) == null) ? 0 :
                                (recyclerView.getChildAt(0).getTop() - recyclerView.getPaddingTop()));
                startActivity(intent);
            }
        };
    }
}
