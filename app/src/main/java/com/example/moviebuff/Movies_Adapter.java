package com.example.moviebuff;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Movies_Adapter extends RecyclerView.Adapter<Movies_Adapter.MyViewHolder> {

    private Activity mContext;
    private List<Movie> movieList;

    public Movies_Adapter(Activity mContext,List<Movie> movieList){
        this.mContext=mContext;
        this.movieList=movieList;
    }
    @NonNull
    @Override
    public Movies_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Movies_Adapter.MyViewHolder holder, int position) {
        holder.title.setText(movieList.get(position).getOriginalTitle());
        Log.i("IMAGE: ",movieList.get(position).getPosterPath());
//        Glide.with(mContext)
//                .load(baseurl+movieList.get(position).getPosterPath())
//                .placeholder(R.drawable.holder)
//                .into(holder.thumbnail);
        Glide.with(mContext)
                .load(movieList.get(position).getPosterPath()).fitCenter()
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;
        public MyViewHolder(View view){
            super(view);
            title=view.findViewById(R.id.title);
            thumbnail=view.findViewById(R.id.post_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();

                    Log.i("SELECTED:","YES"+pos);
                    if(pos!=RecyclerView.NO_POSITION){
                        Movie clickedDataItem=movieList.get(pos);
                        Intent intent=new Intent(mContext,MovieDetail.class);
                        intent.putExtra("original_title",movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                        intent.putExtra("overview",movieList.get(pos).getOverview());
                        intent.putExtra("backdrop_path",movieList.get(pos).getBackdropPath());
                        intent.putExtra("movie_rating",movieList.get(pos).getVoteAverage());
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        intent.putExtra("vote_count",movieList.get(pos).getVoteCount());
                        intent.putExtra("popularity",movieList.get(pos).getPopularity());
                        intent.putExtra("movie_id",movieList.get(pos).getId());
                        intent.putExtra("language",movieList.get(pos).getOriginalLanguage());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,thumbnail,"animate");
                        mContext.startActivity(intent,options.toBundle());
                        Toast.makeText(view.getContext(),"You Clicked: "+clickedDataItem.getOriginalTitle(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
