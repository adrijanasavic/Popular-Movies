package com.example.popular_movies.activities;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.popular_movies.R;
import com.example.popular_movies.adapters.TrailerAdapter;
import com.example.popular_movies.databinding.ActivitySeeAllBinding;
import com.example.popular_movies.models.Trailer;
import com.example.popular_movies.network.TrailerViewModel;


import java.util.List;

public class SeeAllActivity extends AppCompatActivity {

    private ActivitySeeAllBinding binding;
    private TrailerAdapter trailerAdapter;
    private TrailerViewModel trailerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView( this, R.layout.activity_see_all );

        trailerViewModel = ViewModelProviders.of( this ).get( TrailerViewModel.class );

        setupRecyclerViews();

        getTrailers();
    }

    private void setupRecyclerViews() {

        binding.listOfTrailers.setHasFixedSize( true );
        binding.listOfTrailers.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
    }

    public void getTrailers() {
        trailerViewModel.getAllTrailers().observe( this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                trailerAdapter = new TrailerAdapter( getApplicationContext(), trailers );

                if (trailers != null && trailers.isEmpty()) {
                    binding.listOfTrailers.setVisibility( View.GONE );
                    binding.noTrailers.setVisibility( View.VISIBLE );
                }

                binding.listOfTrailers.setAdapter( trailerAdapter );
            }
        } );
    }

}
