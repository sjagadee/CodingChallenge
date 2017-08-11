package com.midtronics.srinivas.codingchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by srinivas on 6/21/17.
 *
 * This class Loads the list of countries which is fetched from countries.xml
 * which is present in resource > values folder
 */

public class CountriesListActivity extends AppCompatActivity {

    private static final String TAG = "CountriesListActivity";

    private ListView lvCountries;
    private String[] countries;
    private List<String> countriesList;
    private ArrayAdapter adapter;
    private SearchView svSearchCountries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries_list_layout);

        Log.d(TAG, "onCreate: Started Oncreate");

        lvCountries = (ListView) findViewById(R.id.lvCountries);
        // This is Search view, which helps in searching the country
        svSearchCountries = (SearchView) findViewById(R.id.svSearchCountries);
        svSearchCountries.setQueryHint("Search...");
        svSearchCountries.onActionViewExpanded();
        svSearchCountries.setIconified(true);

        // Getting countries string array and adding it to Array list
        countries = getResources().getStringArray(R.array.countries_array);
        countriesList = new ArrayList<>();
        countriesList = Arrays.asList(countries);

        // Setup adapter with simple array adapter
        adapter = new ArrayAdapter(this, R.layout.list_items_layout, countriesList);
        lvCountries.setAdapter(adapter);

        // To handle search view
        svSearchCountries.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // To handle the click of list element
        lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Log.d(TAG, "onItemClick: going to detail activity of " + lvCountries.getItemAtPosition(pos).toString());

                Intent intent = new Intent(getApplicationContext(), CountryDetailActivity.class);
                intent.putExtra("country", lvCountries.getItemAtPosition(pos).toString());
                startActivity(intent);
            }
        });

    }
}
