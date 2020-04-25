package com.example.popular_movies.network;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.popular_movies.models.Movie;

import static com.example.popular_movies.network.MovieDataSourceFactory.movieDataSource;


public class MovieViewModel extends ViewModel {

    public LiveData<PagedList<Movie>> moviePagedList;
    private LiveData<PageKeyedDataSource<Integer, Movie>> liveDataSource;

    public MovieViewModel() {

        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory();

        liveDataSource = movieDataSourceFactory.getMovieLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders( false )
                        .setPageSize( MovieDataSource.PAGE_SIZE ).build();

        moviePagedList = (new LivePagedListBuilder( movieDataSourceFactory, pagedListConfig )).build();
    }

    public void clear() {
        movieDataSource.invalidate();
    }
}
