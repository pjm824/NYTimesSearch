package com.pjm284.nytimessearch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pjm284.nytimessearch.R;
import com.pjm284.nytimessearch.models.Article;
import com.pjm284.nytimessearch.models.Photo;
import com.pjm284.nytimessearch.utils.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> mArticles;
    private Context mContext;

    static private OnItemClickListener listener;


    // Pass in the contact array into the constructor
    public ArticleAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    private Context getContext() {
        return mContext;
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        // Set item views based on your views and data model
        DynamicHeightImageView ivImage = viewHolder.ivImage;
        // clear out recycled image from convertView last time
        ivImage.setImageResource(0);

        Photo photo = article.getPhoto();

        if (photo != null) {
            ivImage.setHeightRatio(((double)photo.getHeight())/photo.getWidth());
            Picasso.with(getContext()).load(photo.getUrl())
                    .into(ivImage);
        } else {
            ivImage.setImageResource(R.drawable.image_placeholder);
        }

        TextView tvTitle = viewHolder.tvTitle;
        tvTitle.setText(article.getHeadline());
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements Target {
        public DynamicHeightImageView ivImage;
        public TextView tvTitle;

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Calculate the image ratio of the loaded bitmap
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            // Set the ratio for the image
            ivImage.setHeightRatio(ratio);
            // Load the image into the view
            ivImage.setImageBitmap(bitmap);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivImage = (DynamicHeightImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

    }
}
