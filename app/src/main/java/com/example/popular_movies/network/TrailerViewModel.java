package com.example.popular_movies.network;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popular_movies.models.Trailer;

import java.util.List;

public class TrailerViewModel extends AndroidViewModel {

    private TrailerRepository trailerRepository;

    public TrailerViewModel(@NonNull Application application) {
        super( application );
        trailerRepository = new TrailerRepository( application );
    }

    public LiveData<List<Trailer>> getAllTrailers() {
        return trailerRepository.getMutableLiveData();
    }
}
