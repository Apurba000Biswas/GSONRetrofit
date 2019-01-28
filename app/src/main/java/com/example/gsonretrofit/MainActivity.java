package com.example.gsonretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String CHUCK_NORIS_BASE_URL = "http://api.icndb.com";
    private static final String THE_CAT_API_BASE_URL = "https://api.thecatapi.com";


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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_CAT_API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        TheCatAPI catsAPi = retrofit.create(TheCatAPI.class);
        Call<String> catsCall = catsAPi.catImageUrl(6);
        processCatsUrlResult(catsCall);
    }

    private void processCatsUrlResult(Call<String> catsCall){
        catsCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().toString();
                processCatUrl(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void processCatUrl(String jsonResult){
        GridLayout gridLayout = findViewById(R.id.gl_images);
        gridLayout.removeAllViews();
        try{
            JSONArray jsArray = new JSONArray(jsonResult);
            for (int i=0; i<jsArray.length(); i++){
                String imgUrl = jsArray.getJSONObject(i).getString("url");
                loadImage(imgUrl);
            }

        } catch (JSONException jsEx){
            Log.v("processJsonCat", jsEx.getMessage());
        }
    }
    private void loadImage(String url){
        ImageView img = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        img.setLayoutParams(params);
        GridLayout gridLayout = findViewById(R.id.gl_images);
        gridLayout.addView(img);

        Picasso.get()
                .load(url)
                .resize(550, 800)
                .centerCrop()
                .into(img);
    }
}
