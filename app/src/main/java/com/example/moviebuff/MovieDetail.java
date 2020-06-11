package com.example.moviebuff;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class MovieDetail extends AppCompatActivity {
    TextView nameOfMovie,synopsisMovie,release,rating,vote_count,id,popularity,lang;
    ImageView bg_img,fg_img;
    String movieName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                .inflateTransition(R.transition.shared_transition));
        initCollapsingToolbar();
        vote_count=findViewById(R.id.movie_vote_count);
        id=findViewById(R.id.movie_id);
        popularity=findViewById(R.id.movie_popularity);
        lang=findViewById(R.id.movie_language);
        nameOfMovie=findViewById(R.id.movie_title);
        synopsisMovie=findViewById(R.id.overview_detail);
        release=findViewById(R.id.release_date);
        rating=findViewById(R.id.rating);
        fg_img=findViewById(R.id.poster_fg);
        bg_img=findViewById(R.id.poster_bg);
        Intent receivedIntent=getIntent();
        if(receivedIntent.hasExtra("original_title")){
            String img_path=receivedIntent.getExtras().getString("poster_path");
            movieName=receivedIntent.getExtras().getString("original_title");
            String synopsis=getIntent().getExtras().getString("overview");
            String dor=getIntent().getExtras().getString("release_date");
            String bg=getIntent().getExtras().getString("backdrop_path");
            Double rate=getIntent().getExtras().getDouble("movie_rating");
            Double popular=getIntent().getExtras().getDouble("popularity");
            int movie_id=getIntent().getExtras().getInt("movie_id");
            String language=getIntent().getExtras().getString("language");
            int vote=getIntent().getExtras().getInt("vote_count");
            Log.i("BG_IMG: ",bg);
            Log.i("FG_IMG: ",img_path);
            Glide.with(getApplicationContext())
                    .load(bg).centerInside()
                    .into(bg_img);
            Glide.with(getApplicationContext())
                    .load(img_path)
                    .into(fg_img);
            nameOfMovie.setText(movieName);
            release.setText(dor);
            rating.setText(rate.toString());
            synopsisMovie.setText(synopsis);
            popularity.setText(popular.toString());
            id.setText(String.valueOf(movie_id));
            lang.setText(language);
            vote_count.setText(String.valueOf(vote));
        }else{
            Toast.makeText(this,"No API Data",Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout=
                findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setTitle(movieName);
        AppBarLayout appBarLayout=findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        collapsingToolbarLayout.setTitle(movieName);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    collapsingToolbarLayout.setTitle(""+movieName);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            //    NavUtils.navigateUpFromSameTask(this);
                this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
              return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
