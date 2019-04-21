package com.msn.julio_net.desafiobtg.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msn.julio_net.desafiobtg.R;
import com.msn.julio_net.desafiobtg.controllers.MovieController;
import com.msn.julio_net.desafiobtg.filters.FilterFilmes;
import com.msn.julio_net.desafiobtg.fragments.FavoritosFragment;
import com.msn.julio_net.desafiobtg.models.Movie;
import com.msn.julio_net.desafiobtg.utils.Constantes;
import com.msn.julio_net.desafiobtg.utils.ConvertHelper;
import com.msn.julio_net.desafiobtg.utils.ImageHelper;

import java.lang.ref.WeakReference;
import java.util.List;

public class FilmesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context _context;
    private List<Movie> _filmes;
    private boolean _favorito;
    private FilmeOnClickListener _filmeOnClickListener;
    private FilmeOnLoadMoreListener _filmeOnLoadMoreListener;

    private FilterFilmes filter;
    private FavoritosFragment _fragment;

    private int visibleThreshold = 5;
    private boolean _loading;
    private boolean _searching;

    public FilmesAdapter(Context context, List<Movie> filmes, boolean favorito, FilmeOnClickListener filmeOnClickListener,
                         FavoritosFragment fragment, RecyclerView recyclerView)  {
        this._context = context;
        this._filmes = filmes;
        this._favorito = favorito;
        this._fragment = fragment;
        this._filmeOnClickListener = filmeOnClickListener;
        this.filter = new FilterFilmes(_filmes, this);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (linearLayoutManager != null) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!_searching && !_loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (_filmeOnLoadMoreListener != null) {
                            _filmeOnLoadMoreListener.onLoadMore();
                        }
                        _loading = true;
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listrow_filme, parent, false);
        return new FilmesAdapter.ViewHolderFilmes(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FilmesAdapter.ViewHolderFilmes h = (FilmesAdapter.ViewHolderFilmes)holder;
        final int p = position;

        final Movie filme = _filmes.get(position);
        h.textViewNome.setText(filme.getTitle());
        h.textViewAno.setText(String.format("(%s)", filme.getRelease_date().substring(0, 4)));
        h.imageViewPoster.setImageBitmap(ConvertHelper.ByteArrayToBitmap(filme.getPoster()));

        if (filme.getPoster() == null) {
            String url = Constantes.URL_IMAGES + filme.getPoster_path();
            new DownloadImageTask(_context, h.imageViewPoster, filme).execute(url);
        }

        if (_filmeOnClickListener != null) {
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _filmeOnClickListener.onClickItem(h.itemView, p);
                }
            });
        }

        if (filme.getFavorite()) {
            h.imageViewFavorite.setImageResource(R.drawable.ic_action_favorite);
        } else {
            h.imageViewFavorite.setImageResource(R.drawable.ic_action_unfavorite);
        }

        h.imageViewFavorite.setVisibility(_favorito ? View.VISIBLE : View.GONE);
        h.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filme.setFavorite(!filme.getFavorite());
                MovieController.alterar(_context, filme);

                if (filme.getFavorite()) {
                    h.imageViewFavorite.setImageResource(R.drawable.ic_action_favorite);
                } else {
                    h.imageViewFavorite.setImageResource(R.drawable.ic_action_unfavorite);
                }

                if (_fragment != null) {
                    _fragment.updateAdapterRecyclerView();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return _filmes.size();
    }

    public void setOnLoadMoreListener(FilmeOnLoadMoreListener onLoadMoreListener) {
        this._filmeOnLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded(boolean loading) {
        this._loading = loading;
    }

    public void setSearched(boolean searching) {
        this._searching = searching;
    }

    private class ViewHolderFilmes extends RecyclerView.ViewHolder {

        ImageView imageViewPoster;
        TextView textViewNome;
        TextView textViewAno;
        ImageView imageViewFavorite;

        ViewHolderFilmes(final View view) {
            super(view);

            imageViewPoster = view.findViewById(R.id.image_view_poster_filme);
            textViewNome = view.findViewById(R.id.text_view_nome_filme);
            textViewAno = view.findViewById(R.id.text_view_ano_filme);
            imageViewFavorite = view.findViewById(R.id.image_view_favorito);
        }
    }

    public List<Movie> getList() {
        return this._filmes;
    }

    public void setList(List<Movie> list) {
        this._filmes = list;
    }

    public void filterList(String text) {
        filter.filter(text);
    }

    public interface FilmeOnClickListener {
        void onClickItem(View view, int i);
    }

    public interface FilmeOnLoadMoreListener {
        void onLoadMore();
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<Context> _reference;
        private Movie _movie;
        private WeakReference<ImageView> _imageView;

        DownloadImageTask(Context context, ImageView imageView, Movie movie) {
            this._reference = new WeakReference<>(context);
            this._imageView = new WeakReference<>(imageView);
            this._movie = movie;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bmp = null;
            try {
                bmp = ImageHelper.downloadImageBitmap(urls[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            Context context = _reference.get();
            if (result != null) {
                _movie.setPoster(ImageHelper.convertBitmapToByteArray(result));
                MovieController.alterar(context, _movie);
            }
            if (_imageView.get() != null) {
                _imageView.get().setImageBitmap(result);
            }
        }
    }
}
