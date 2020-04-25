package com.example.popular_movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popular_movies.R;
import com.example.popular_movies.activities.WebViewActivity;
import com.example.popular_movies.models.Review;


import java.util.List;

import static com.example.popular_movies.utils.Constant.URL_OF_REVIEW;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;

    private List<Review> reviewList;

    private int mItemSelected = -1;


    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.review_item, parent, false );
        return new ReviewViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review currentReview = reviewList.get( position );
        holder.reviewOfMovie.setText( currentReview.getContent() );
        holder.authorOfReview.setText( "written by" + " " + currentReview.getAuthor() );
    }


    @Override
    public int getItemCount() {
        if (reviewList == null) {
            return 0;
        }
        return reviewList.size();
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewOfMovie;
        TextView authorOfReview;
        TextView urlOfReview;

        private ReviewViewHolder(View itemView) {
            super( itemView );
            reviewOfMovie = itemView.findViewById( R.id.reviewOfMovie );
            authorOfReview = itemView.findViewById( R.id.authorOfReview );
            urlOfReview = itemView.findViewById( R.id.urlOfReview );

            urlOfReview.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( v.getContext(), WebViewActivity.class );

                    mItemSelected = getAdapterPosition();
                    notifyDataSetChanged();

                    String url = reviewList.get( mItemSelected ).getUrl();

                    intent.putExtra( URL_OF_REVIEW, url );
                    v.getContext().startActivity( intent );
                }
            } );
        }
    }
}
