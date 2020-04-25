package com.example.popular_movies.network;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.popular_movies.models.Review;
import com.example.popular_movies.models.ReviewApiResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.popular_movies.activities.MovieActivity.idOfMovie;


public class ReviewRepository {

    private List<Review> reviewList = new ArrayList<>();
    private MutableLiveData<List<Review>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public ReviewRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Review>> getMutableLiveData() {
        RetrofitClient.getInstance()
                .getMovieService().getReviews( (idOfMovie), MovieService.API_KEY )
                .enqueue( new Callback<ReviewApiResponse>() {
                    @Override
                    public void onResponse(Call<ReviewApiResponse> call, Response<ReviewApiResponse> response) {
                        Log.v( "onResponse", "Succeeded reviews" );
                        if (response.body() != null) {
                            reviewList = response.body().getReviews();
                            mutableLiveData.setValue( reviewList );
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewApiResponse> call, Throwable t) {
                        Log.v( "onFailure", "Failed to get Reviews" );
                    }
                } );

        return mutableLiveData;
    }
}
