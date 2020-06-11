package com.example.moviebuff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
//    Window window;
//    ActionBar bar;
    private RecyclerView recyclerView;
    private Movies_Adapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
//    AlertDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final String LOG_TAG=Movies_Adapter.class.getName();
    public static final String THE_MOVIE_DB_API_TOKEN="6afd3d7e065a3911166d9ce05459bac3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        window=getWindow();
//        window.setStatusBarColor(0xffc62827);
//        bar=getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(0xffe53935));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recyclerView);
        initViews();
        swipeRefreshLayout=findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this,"Movies Refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Activity getActivity(){
        Context context=this;
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity) context;
            }
            context=((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    private void initViews(){
        pd=new ProgressDialog(this);
        pd.setMessage("Fetching movies...");
        pd.setCancelable(false);
        pd.show();

        movieList=new ArrayList<>();
        adapter=new Movies_Adapter(this,movieList);
        if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadJSON();
    }

    private void loadJSON(){
        try{
            if(THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please obtain API Key first",Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            Client client=new Client();
            Service apiService=Client.getClient().create(Service.class);
            Call<MoviesResponse> call=apiService.getPopularMovies(THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies=response.body().getResults();
                    recyclerView.setAdapter(new Movies_Adapter(getActivity(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error: ",t.getMessage());
                    pd.dismiss();
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("ERROR!");
                    builder.setMessage("Sorry there was an error getting data from Intenet. \n" +
                            "Network Unavailable!")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            initViews();
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.show();
                    Toast.makeText(getApplicationContext(),"Error Fetching data!",Toast.LENGTH_SHORT).show();

                }
            });
        }catch(Exception e){
            Log.d("Error: ",e.getMessage());
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings: return true;
            case android.R.id.home: return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}