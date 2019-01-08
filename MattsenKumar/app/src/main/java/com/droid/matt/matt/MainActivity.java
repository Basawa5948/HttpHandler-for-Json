package com.droid.matt.matt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ListView listView;
    Bundle bundle;
    JSONObject jsonObject;
    RecyclerView.Adapter adapter;

    private static final int TIME = 2000;
    private long backPressed;

    //RetroFit Approach
    public String API_KEY = "10977577-f0b29d91d6b843c7d6b5b7ecc";

    //Normal Json Parsing
    private static String url = "https://pixabay.com/api/?key=10977577-f0b29d91d6b843c7d6b5b7ecc";

    private String TAG = MainActivity.class.getSimpleName();

    ArrayList<HashMap<String,String>> finallist;

    //Double back pressed for Exit
    @Override
    public void onBackPressed(){
        if(backPressed+TIME > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
        }else{
            Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        finallist = new ArrayList<>();
        bundle = new Bundle();
        bundle = getIntent().getExtras();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(MainActivity.this,Login.class));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        progressDialog.show();
                        progressDialog.setMessage("Logging You Out");
                        firebaseAuth.signOut();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this,Login.class));
                                finish();
                            }
                        }, 3000); // 3000 milliseconds delay
                    }
                });

        new GetImageUrl().execute();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getDataFromUrl();

    }

    private void getDataFromUrl() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRequest.Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRequest apiRequest = retrofit.create(ApiRequest.class);

        Call<List<Images>> call = apiRequest.getDataFromUrl(API_KEY);

            call.enqueue(new Callback<List<Images>>() {
                @Override
                public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                    Log.d("ResponseRetroFit","Number :"+response.body());
                    List<Images> myResponseList = response.body();
                }

                @Override
                public void onFailure(Call<List<Images>> call, Throwable t) {

                }
            });
    }

    private class GetImageUrl extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if(jsonStr!=null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    JSONArray jsonArray = jsonObject.getJSONArray("hits");

                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject images = jsonArray.getJSONObject(i);

                        String imageurl = images.getString("largeImageURL");
                        String user = images.getString("user");

                        HashMap<String,String> middlelist = new HashMap<>();
                        middlelist.put("LargerImageUrl",imageurl);
                        middlelist.put("USER",user);

                        finallist.add(middlelist);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }else{
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            //List View Approach
            /**
             * Updating parsed JSON data into ListView
             * */
            /*ListAdapter adapter = new SimpleAdapter(MainActivity.this,finallist,
                    R.layout.list_items_second,
                    new String[]{"LargerImageUrl","USER"},
                    new int[]{R.id.thumb,R.id.user});

            listView.setAdapter(adapter);*/

            //ListView is not a standard approach to laod Images or Play Videos. So I am using my own Adapter for recyclerview

            //Calling the Adapter class for binding the data to the recycler view
            adapter = new ImageAdapter(finallist,MainActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }
}
