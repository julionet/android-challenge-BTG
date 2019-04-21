package com.msn.julio_net.desafiobtg.filters;

import android.widget.Filter;

import com.msn.julio_net.desafiobtg.adapters.FilmesAdapter;
import com.msn.julio_net.desafiobtg.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class FilterFilmes extends Filter {

    private List<Movie> _moviesList;
    private List<Movie> _filteredMoviesList;
    private FilmesAdapter _adapter;

    public FilterFilmes(List<Movie> movieList, FilmesAdapter adapter) {
        this._adapter = adapter;
        this._moviesList = movieList;
        this._filteredMoviesList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        _filteredMoviesList.clear();
        final FilterResults results = new FilterResults();

        for (final Movie item : _moviesList) {
            if (item.getTitle().toLowerCase().trim().contains(charSequence) ||
                    (item.getRelease_date().substring(0, 4).contains(charSequence))) {
                _filteredMoviesList.add(item);
            }
        }

        results.values = _filteredMoviesList;
        results.count = _filteredMoviesList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        _adapter.setList(_filteredMoviesList);
        _adapter.notifyDataSetChanged();
    }
}
