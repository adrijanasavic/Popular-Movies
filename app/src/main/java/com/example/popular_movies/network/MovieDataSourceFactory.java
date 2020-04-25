package com.example.popular_movies.network;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.popular_movies.models.Movie;

public class MovieDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Movie>> movieLiveDataSource = new MutableLiveData<>();

    static MovieDataSource movieDataSource;

    @Override
    public DataSource<Integer, Movie> create() {
        movieDataSource = new MovieDataSource();

        movieLiveDataSource.postValue( movieDataSource );

        return movieDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Movie>> getMovieLiveDataSource() {
        return movieLiveDataSource;
    }
}
