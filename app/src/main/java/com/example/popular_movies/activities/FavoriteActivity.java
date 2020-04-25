package com.example.popular_movies.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popular_movies.R;
import com.example.popular_movies.adapters.FavoriteAdapter;
import com.example.popular_movies.database.MovieRoomViewModel;
import com.example.popular_movies.models.Movie;
import com.example.popular_movies.utils.SharedPreferencesUtils;


import java.util.List;


public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.FavoriteAdapterOnClickHandler {

    public static String MOVIE = "movie";
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private MovieRoomViewModel movieRoomViewModel;
    private TextView noBookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_favorite );

        recyclerView = findViewById( R.id.favorite_list );
        recyclerView.setLayoutManager( new GridLayoutManager( this, (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 3 : 4 ) );
        recyclerView.setHasFixedSize( true );
        noBookmarks = findViewById( R.id.noBookmarks );

        favoriteAdapter = new FavoriteAdapter( this, this );
        movieRoomViewModel = ViewModelProviders.of( this ).get( MovieRoomViewModel.class );

        loadMoviesFromDatabase();
    }


    private void loadMoviesFromDatabase() {
        movieRoomViewModel.getAllFavoriteMovies().observe( this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> favoriteMovies) {
                favoriteAdapter.submitList( favoriteMovies );
                Log.v( "favoriteList", favoriteMovies.size() + "" );
                if (favoriteMovies.isEmpty()) {
                    noBookmarks.setVisibility( View.VISIBLE );
                    recyclerView.setVisibility( View.GONE );
                    noBookmarks.setText( getString( R.string.no_bookmarks ) );
                }
            }
        } );

        recyclerView.setAdapter( favoriteAdapter );
        favoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent( FavoriteActivity.this, MovieActivity.class );
        intent.putExtra( MOVIE, (movie) );
        startActivity( intent );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.favorite, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all) {
            movieRoomViewModel.deleteAll();
            SharedPreferencesUtils.clearSharedPreferences( this );
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}

