package com.example.popular_movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.popular_movies.R;
import com.example.popular_movies.models.Movie;


import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;

    private List<Movie> movieList;

    private Movie currentMovie;


    private SearchAdapterOnClickHandler clickHandler;


    public interface SearchAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public SearchAdapter(Context mContext, List<Movie> movieList, SearchAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.movieList = movieList;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.movie_item, parent, false );
        return new SearchViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        currentMovie = movieList.get( position );
        holder.movieTitle.setText( currentMovie.getMovieTitle() );
        holder.movieRating.setText( currentMovie.getMovieVote() );

        String imageUrl2 = "https://image.tmdb.org/t/p/w500";
        Glide.with( mContext )
                .load( imageUrl2 + currentMovie.getMoviePoster() )

                .into( holder.moviePoster );
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    public void clear() {
        int size = movieList.size();
        movieList.clear();
        notifyItemRangeRemoved( 0, size );
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView movieTitle;
        TextView movieRating;
        ImageView moviePoster;

        private SearchViewHolder(View itemView) {
            super( itemView );
            movieTitle = itemView.findViewById( R.id.movie_title );
            movieRating = itemView.findViewById( R.id.movie_rating );
            moviePoster = itemView.findViewById( R.id.movie_poster );

            itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            currentMovie = movieList.get( position );

            clickHandler.onClick( currentMovie );
        }
    }
}
