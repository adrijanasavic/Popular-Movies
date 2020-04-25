package com.example.popular_movies.network;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.popular_movies.models.Trailer;
import com.example.popular_movies.models.TrailerApiResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrailerRepository {

    private static final String API_KEY = "API KEY";
    private List<Trailer> trailerList = new ArrayList<>();
    private MutableLiveData<List<Trailer>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public static String idOfMovie;

    public TrailerRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Trailer>> getMutableLiveData() {
        RetrofitClient.getInstance()
                .getMovieService().getTrailers( (idOfMovie), API_KEY )
                .enqueue( new Callback<TrailerApiResponse>() {
                    @Override
                    public void onResponse(Call<TrailerApiResponse> call, Response<TrailerApiResponse> response) {
                        Log.v( "onResponse", "Succeeded Trailers" );
                        if (response.body() != null) {
                            trailerList = response.body().getTrailers();
                            mutableLiveData.setValue( trailerList );
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerApiResponse> call, Throwable t) {
                        Log.v( "onFailure", "Failed to get Trailers" );
                    }
                } );

        return mutableLiveData;
    }
}
