package com.example.gsonretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String CHUCK_NORIS_BASE_URL = "http://api.icndb.com";
    private static final String THE_CAT_API = "https://api.thecatapi.com/api/images/get?format=json&results_per_page=6";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickJocks(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CHUCK_NORIS_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        ChuckNorrisAPI chuck = retrofit.create(ChuckNorrisAPI.class);
        Call<String> joke = chuck.randomJoke();
        processRandomJokeResult(joke);

    }
    private void processRandomJokeResult(Call<String> jokeCall){
        jokeCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    String result = response.body().toString();
                    extractJoke(result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("processRandomJokeResult", "No internet connection");
            }
        });

    }

    private void extractJoke(String jsonResult){
        try{
            JSONObject jsJoke = new JSONObject(jsonResult);
            String joke = jsJoke.getJSONObject("value")
                    .getString("joke");
            TextView tvJoke = findViewById(R.id.tv_joke);
            tvJoke.setText(joke);
        }catch (JSONException jsE){
            Log.v("extractJoke", jsE.getMessage());
        }
    }

    public void onClickCats(View view) {
    }
}
/* Json format -------------------
        {
          "type": "success",
          "value": {
            "id": 114,
            "joke": "Chuck Norris doesn't believe in Germany.",
            "categories": [

            ]
          }
        }
         */