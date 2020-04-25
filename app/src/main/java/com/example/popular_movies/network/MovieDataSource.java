package com.example.popular_movies.network;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.popular_movies.activities.MainActivity;
import com.example.popular_movies.models.Movie;
import com.example.popular_movies.models.MovieApiResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE = 1;

    static final int PAGE_SIZE = 20;
    String API_KEY = "API KEY";


    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        RetrofitClient.getInstance()
                .getMovieService().getMovies( MainActivity.getSort(), FIRST_PAGE, API_KEY )
                .enqueue( new Callback<MovieApiResponse>() {
                    @Override
                    public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                        MainActivity.progressDialog.dismiss();
                        Log.v( "onResponse", "Succeeded movies" );
                        if (response.body().getMovies() == null) {
                            return;
                        }

                        if (response.body() != null) {

                            callback.onResult( response.body().getMovies(), null, FIRST_PAGE + 1 );
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieApiResponse> call, Throwable t) {
                        Log.v( "onFailure", "Failed to get Movies" );
                        MainActivity.progressDialog.dismiss();
                    }
                } );


    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        RetrofitClient.getInstance()
                .getMovieService().getMovies( MainActivity.getSort(), params.key, API_KEY )
                .enqueue( new Callback<MovieApiResponse>() {
                    @Override
                    public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {

                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {

                            callback.onResult( response.body().getMovies(), adjacentKey );
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieApiResponse> call, Throwable t) {
                    }
                } );
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        RetrofitClient.getInstance()
                .getMovieService().getMovies( MainActivity.getSort(), params.key, API_KEY )
                .enqueue( new Callback<MovieApiResponse>() {
                    @Override
                    public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                        if (response.body() != null) {

                            Integer key = response.body().getMovies().size() == PAGE_SIZE ? params.key + 1 : null;


                            callback.onResult( response.body().getMovies(), key );
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieApiResponse> call, Throwable t) {
                    }
                } );
    }
}
