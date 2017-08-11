package com.midtronics.srinivas.codingchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.midtronics.srinivas.codingchallenge.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by srinivas on 6/21/17.
 *
 * This Activity fetches the selected country data and display the data
 * on the screen, for fetching the JSON data from the server, I have used
 * Retrofit API
 */

public class CountryDetailActivity extends AppCompatActivity {

    private static final String TAG = "CountryDetailActivity";

    private TextView tvCountryName;
    private TextView tvCapital;
    private TextView tvPopulation;
    private TextView tvArea;
    private TextView tvRegion;
    private TextView tvSubRegion;
    private Button button;
    private EditText etNameUpdate;

    // Base url
    private String BASE_URL = "https://restcountries.eu/rest/v1/";
    private CountryAPI countryAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_detail_layout);

        Log.d(TAG, "onCreate: Started OnCreate");

        tvCountryName = (TextView) findViewById(R.id.tvCountryName);
        tvCapital = (TextView) findViewById(R.id.tvCapital);
        tvPopulation = (TextView) findViewById(R.id.tvPopulation);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvSubRegion = (TextView) findViewById(R.id.tvSubRegion);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String country = (String) bundle.get("country");
            tvCountryName.setText(country);
            fetchInformation(country);
        }

        button = (Button) findViewById(R.id.bUpdate);
        etNameUpdate = (EditText) findViewById(R.id.etNameToUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etNameUpdate != null) {
                    sendMessage(etNameUpdate.getText().toString());
                }
            }
        });
    }

    /*
     * Get the JSON information by country name, here in this method
     * Retrofit is initialized and used
     */
    public void fetchInformation(final String country) {

        Log.d(TAG, "fetchInformation: Started Fetching Information");
        
        // Set up retrofit, by creating retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // This is to do Possible API calls using Interface, here retrofit would
        // network calls in the background thread and wait for the response
        countryAPI = retrofit.create(CountryAPI.class);
        Call<List<Country>> call = countryAPI.getCountry(country);

        // The response gets delivered using onResponse() and onFailure() methods on
        // main thread
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                Log.d(TAG, "onResponse: response" + response.toString());

                if (response.code() == 200 ) {

                    Log.d(TAG, "onResponse: response" + response.body().toString());

                    Country curCountry = new Country();
                    List<Country> countryList = response.body();
                    int count = -1;
                    do {
                        count++;
                        if (count == countryList.size()) {
                            Log.e(TAG, "onResponse: Something went wrong");
                            Toast.makeText(getApplicationContext(), "The country data you are searching for is not present!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        curCountry = response.body().get(count);
                    }
                    while (!country.equals(curCountry.getName()));

                    setTheDataToView(curCountry);
                } else {
                    Log.e(TAG, "onResponse: Something went wrong");
                    Toast.makeText(getApplicationContext(), "The country data you are searching for is not present!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e(TAG, "onFailure: Something went wrong " + t.getMessage());
            }
        });
    }

    /*
     * This private method would check if the instance of country data is present or not and
     * set the textview with corresponding retrieved data
     */
    private void setTheDataToView(Country curCountry) {

        Log.d(TAG, "setTheDataToView: Setting up Text View");

        if (curCountry.getCapital() != null && !curCountry.getCapital().isEmpty()) {
            tvCapital.setText(curCountry.getCapital());
        }

        if (curCountry.getPopulation() != null && !curCountry.getPopulation().toString().isEmpty()) {
            tvPopulation.setText(curCountry.getPopulation().toString());
        }

        if (curCountry.getArea() != null && !curCountry.getArea().toString().isEmpty()) {
            tvArea.setText(curCountry.getArea().toString());
        }

        if (curCountry.getRegion() != null && !curCountry.getRegion().isEmpty()) {
            tvRegion.setText(curCountry.getRegion());
        }

        if (curCountry.getSubregion() != null && !curCountry.getSubregion().isEmpty()) {
            tvSubRegion.setText(curCountry.getSubregion());
        }
    }

    // Send an Intent with an action named "custom-event-name". The Intent sent should
    // be received by the ReceiverActivity.
    private void sendMessage(String name) {
        Log.d("sender", "Broadcasting message as " + name);
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("name", name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
