package com.example.popular_movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.popular_movies.R;
import com.example.popular_movies.models.Movie;



public class FavoriteAdapter extends ListAdapter<Movie, FavoriteAdapter.FavoriteHolder> {

    private Context mContext;
    private Movie movie;
    public static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";


    private FavoriteAdapterOnClickHandler clickHandler;


    public interface FavoriteAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public FavoriteAdapter(Context mContext, FavoriteAdapterOnClickHandler clickHandler) {
        super( DIFF_CALLBACK );
        this.mContext = mContext;
        this.clickHandler = clickHandler;
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getMovieId() == newItem.getMovieId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getMovieTitle().equals( newItem.getMovieTitle() ) &&
                    oldItem.getMovieDescription().equals( newItem.getMovieDescription() );
        }
    };

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.movie_item, parent, false );
        return new FavoriteHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
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

            holder.moviePoster.setImageResource( R.drawable.no_preview );
        }
    }

    public Movie getMovieAt(int position) {
        return getItem( position );
    }

    class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView movieTitle;
        TextView movieRating;
        ImageView moviePoster;

        public FavoriteHolder(View itemView) {
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
