package com.example.popular_movies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.popular_movies.R;
import com.example.popular_movies.models.Movie;


public class MovieAdapter extends PagedListAdapter<Movie, MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private Movie movie;
    public static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";


    private MovieAdapterOnClickHandler clickHandler;


    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(Context mContext, MovieAdapterOnClickHandler clickHandler) {
        super( DIFF_CALLBACK );
        this.mContext = mContext;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.movie_item, parent, false );
        return new MovieViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        movie = getItem( position );

        if (movie != null) {
            holder.movieTitle.setText( movie.getMovieTitle() );
            holder.movieRating.setText( movie.getMovieVote() );

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder( R.drawable.maze_ruuner )
                    .error( R.drawable.dots )
                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                    .priority( Priority.HIGH )
                    .dontAnimate()
                    .dontTransform();

            Glide.with( mContext )
                    .load( IMAGE_URL + movie.getMoviePoster() )

                    .into( holder.moviePoster );
        } else {
            Toast.makeText( mContext, "Movie is null", Toast.LENGTH_LONG ).show();
        }
    }

    public Movie getMovieAt(int position) {
        return getItem( position );
    }

    @Override
    public PagedList<Movie> getCurrentList() {
        return super.getCurrentList();
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldMovie, @NonNull Movie newMovie) {
            return oldMovie.movieId.equals( newMovie.movieId );
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldMovie, @NonNull Movie newMovie) {
            return oldMovie.equals( newMovie );
        }
    };

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView movieTitle;
        TextView movieRating;
        ImageView moviePoster;

        private MovieViewHolder(View itemView) {
            super( itemView );
            movieTitle = itemView.findViewById( R.id.movie_title );
            movieRating = itemView.findViewById( R.id.movie_rating );
            moviePoster = itemView.findViewById( R.id.movie_poster );

            itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            movie = getItem( position );

            clickHandler.onClick( movie );
        }
    }
}