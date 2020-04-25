package com.example.popular_movies.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.popular_movies.R;
import com.example.popular_movies.adapters.ReviewAdapter;
import com.example.popular_movies.adapters.TrailerAdapter;
import com.example.popular_movies.database.MovieRoomViewModel;
import com.example.popular_movies.databinding.ActivityMovieBinding;

import com.example.popular_movies.models.Movie;
import com.example.popular_movies.models.Review;
import com.example.popular_movies.models.Trailer;
import com.example.popular_movies.network.ReviewViewModel;
import com.example.popular_movies.network.TrailerViewModel;
import com.example.popular_movies.utils.Genres;
import com.example.popular_movies.utils.SharedPreferencesUtils;
import com.example.popular_movies.utils.Utility;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.example.popular_movies.activities.MainActivity.MOVIE;


public class MovieActivity extends AppCompatActivity {

    private ActivityMovieBinding binding;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private RecyclerView reviewsRecyclerView, trailersRecyclerView;

    private ReviewViewModel reviewViewModel;
    private TrailerViewModel trailerViewModel;
    private MovieRoomViewModel movieRoomViewModel;

    public static String idOfMovie;
    private String title;
    private String formattedDate;
    private String vote;
    private String description;
    private String language;
    private String poster;
    private String backDrop;

    private Movie movie;
    public static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_movie );

        reviewViewModel = ViewModelProviders.of( this ).get( ReviewViewModel.class );
        trailerViewModel = ViewModelProviders.of( this ).get( TrailerViewModel.class );
        movieRoomViewModel = ViewModelProviders.of( this ).get( MovieRoomViewModel.class );

        setupRecyclerViews();

        receiveMovieDetails();

        getReviews();
        getTrailers();

        binding.fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavourite();
            }
        } );
        binding.txtSeaAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSeeAllActivity();
            }
        } );
    }

    private void setupRecyclerViews() {

        trailersRecyclerView = findViewById( R.id.listOfTrailers );
        trailersRecyclerView.setHasFixedSize( true );
        trailersRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false ) );

        reviewsRecyclerView = findViewById( R.id.listOfReviews );
        reviewsRecyclerView.setHasFixedSize( true );
        reviewsRecyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false ) );
    }

    private void receiveMovieDetails() {

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra( MOVIE );

        idOfMovie = movie.getMovieId();
        title = movie.getMovieTitle();
        vote = movie.getMovieVote();
        description = movie.getMovieDescription();
        formattedDate = Utility.formatDate( movie.getMovieReleaseDate() );
        language = movie.getMovieLanguage();
        backDrop = movie.getMovieBackdrop();
        poster = movie.getMoviePoster();

        binding.titleOfMovie.setText( title );
        binding.ratingOfMovie.setText( vote );
        binding.descriptionOfMovie.setText( description );
        binding.releaseDateOfMovie.setText( formattedDate + " " + "|" );
        binding.languageOfMovie.setText( language );

        Glide.with( this )
                .load( IMAGE_URL + backDrop )
                .into( binding.backdropImage );

        getGenres();

        if (!isNetworkConnected()) {
            trailersRecyclerView.setVisibility( View.GONE );
            binding.noTrailers.setVisibility( View.VISIBLE );

            reviewsRecyclerView.setVisibility( View.GONE );
            binding.noReviews.setVisibility( View.VISIBLE );
        }

        if (SharedPreferencesUtils.getInsertState( this, idOfMovie )) {
            binding.fab.setImageResource( R.drawable.favorite_red );
        }
    }

    public void getTrailers() {
        trailerViewModel.getAllTrailers().observe( this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                trailerAdapter = new TrailerAdapter( getApplicationContext(), trailers );

                if (trailers != null && trailers.isEmpty()) {
                    trailersRecyclerView.setVisibility( View.GONE );
                    binding.noTrailers.setVisibility( View.VISIBLE );
                }

                trailersRecyclerView.setAdapter( trailerAdapter );
            }
        } );
    }

    public void getReviews() {
        reviewViewModel.getAllReviews().observe( this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                reviewAdapter = new ReviewAdapter( getApplicationContext(), reviews );

                if (reviews != null && reviews.isEmpty()) {
                    reviewsRecyclerView.setVisibility( View.GONE );
                    binding.noReviews.setVisibility( View.VISIBLE );
                }

                reviewsRecyclerView.setAdapter( reviewAdapter );
            }
        } );
    }

    private void getGenres() {
        int genre_one = 0;
        int genre_two = 0;
        int genre_three = 0;

        try {
            genre_one = movie.getGenreIds().get( 0 );
            genre_two = movie.getGenreIds().get( 1 );
            genre_three = movie.getGenreIds().get( 2 );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (movie.getGenreIds() == null) {
            binding.genreOne.setVisibility( View.GONE );
            binding.genreTwo.setVisibility( View.GONE );
            binding.genreThree.setVisibility( View.GONE );
        }

        binding.genreOne.setText( Genres.getGenres().get( genre_one ) );
        binding.genreTwo.setText( Genres.getGenres().get( genre_two ) );
        binding.genreThree.setText( Genres.getGenres().get( genre_three ) );

    }

    private void toggleFavourite() {

        if (!SharedPreferencesUtils.getInsertState( this, idOfMovie )) {
            binding.fab.setImageResource( R.drawable.favorite_red );
            insertFavoriteMovie();
            SharedPreferencesUtils.setInsertState( this, idOfMovie, true );
            showSnackBar( "Bookmark Added" );
        } else {
            binding.fab.setImageResource( R.drawable.favorite_border_red );
            deleteFavoriteMovieById();
            SharedPreferencesUtils.setInsertState( this, idOfMovie, false );
            showSnackBar( "Bookmark Removed" );
        }
    }


    private void insertFavoriteMovie() {
        movie = new Movie( idOfMovie, title, vote, description, formattedDate, language, poster, backDrop );
        movieRoomViewModel.insert( movie );
    }

    private void deleteFavoriteMovieById() {
        movieRoomViewModel.deleteById( Integer.parseInt( idOfMovie ) );
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showSnackBar(String text) {
        Snackbar.make( findViewById( android.R.id.content ), text, Snackbar.LENGTH_SHORT ).show();
    }

    private void goToSeeAllActivity() {
        Intent intent = new Intent( this, SeeAllActivity.class );
        startActivity( intent );
    }
}
