package com.msn.julio_net.desafiobtg;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.msn.julio_net.desafiobtg.fragments.FavoritosFragment;
import com.msn.julio_net.desafiobtg.fragments.FilmesFragment;
import com.msn.julio_net.desafiobtg.utils.Constantes;
import com.msn.julio_net.desafiobtg.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;

    FilmesFragment fragmentFilme;
    FavoritosFragment fragmentFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager_moveis);
        tabLayout = findViewById(R.id.tabs_movies);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragmentFilme = new FilmesFragment();
        fragmentFavorito = new FavoritosFragment();
        fragmentFilme.setFavoritosFragment(fragmentFavorito);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragmentFilme, "Filmes");
        adapter.addFragment(fragmentFavorito, "Favoritos");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Preferences.getInteger(getBaseContext(), Constantes.VIEW_PAGER_INDEX));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        item.collapseActionView();

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if  (tabLayout.getSelectedTabPosition() == 0) {
                    fragmentFilme._adapter.filterList(newText);
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    fragmentFavorito._adapter.filterList(newText);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ordenar_title:
                if  (tabLayout.getSelectedTabPosition() == 0) {
                    Preferences.setString(getBaseContext(), Constantes.MOVIE_ORDER, "title");
                    fragmentFilme.updateAdapterRecyclerView();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    Preferences.setString(getBaseContext(), Constantes.FAVORITE_ORDER, "title");
                    fragmentFavorito.updateAdapterRecyclerView();
                }
                return true;
            case R.id.menu_ordenar_ano:
                if  (tabLayout.getSelectedTabPosition() == 0) {
                    Preferences.setString(getBaseContext(), Constantes.MOVIE_ORDER, "releasedate");
                    fragmentFilme.updateAdapterRecyclerView();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    Preferences.setString(getBaseContext(), Constantes.FAVORITE_ORDER, "releasedate");
                    fragmentFavorito.updateAdapterRecyclerView();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}
