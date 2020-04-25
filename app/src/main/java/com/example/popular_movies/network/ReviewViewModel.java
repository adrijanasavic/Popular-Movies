package com.example.popular_movies.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popular_movies.models.Review;

import java.util.List;


public class ReviewViewModel extends AndroidViewModel {

    private ReviewRepository reviewRepository;

    public ReviewViewModel(@NonNull Application application) {
        super( application );
        reviewRepository = new ReviewRepository( application );
    }

    public LiveData<List<Review>> getAllReviews() {
        return reviewRepository.getMutableLiveData();
    }
}
