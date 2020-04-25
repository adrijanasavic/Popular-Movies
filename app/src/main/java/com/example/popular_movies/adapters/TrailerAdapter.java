package com.example.popular_movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.popular_movies.R;
import com.example.popular_movies.models.Trailer;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context mContext;

    private List<Trailer> trailerList;


    private int mItemSelected = -1;

    public TrailerAdapter(Context mContext, List<Trailer> trailerList) {
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.trailer_item, parent, false );
        return new TrailerViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer currentMovie = trailerList.get( position );

        String key = currentMovie.getKeyOfTrailer();

        String url = mContext.getString( R.string.youtube_url ) + key;

        String imageId = getYouTubeId( url );
        Log.d( "imageId", imageId );

        Glide.with( mContext )
                .load( mContext.getString( R.string.thumbnail_firstPart ) + imageId + mContext.getString( R.string.thumbnail_secondPart ) )
                .into( holder.trailerOfMovie );
        holder.nameOfTrailer.setText( currentMovie.getNameOfTrailer() );
    }

    @Override
    public int getItemCount() {
        if (trailerList == null) {
            return 0;
        }
        return trailerList.size();
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder {


        ImageView trailerOfMovie;
        TextView nameOfTrailer;

        private TrailerViewHolder(View itemView) {
            super( itemView );
            trailerOfMovie = itemView.findViewById( R.id.trailerOfMovie );
            nameOfTrailer = itemView.findViewById( R.id.nameOfTrailer );

            trailerOfMovie.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mItemSelected = getAdapterPosition();

                    String key = trailerList.get( mItemSelected ).getKeyOfTrailer();
                    String url = v.getContext().getString( R.string.youtube_url ) + key;
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    if (intent.resolveActivity( v.getContext().getPackageManager() ) != null) {
                        v.getContext().startActivity( intent );
                    }
                }
            } );
        }
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile( pattern );
        Matcher matcher = compiledPattern.matcher( youTubeUrl );
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }
}
