package populi.com.populichallenge;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import populi.com.populichallenge.model.Country;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String URL = "http://staticfiles.popguide.me/code_challenge/countries_v1.json";
    private static final String DATA_FILENAME = "ToursData.txt";

    private SwipeRefreshLayout mSwipe;
    private RecyclerView mCountriesListView;
    private List<Country> mCountries;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipe = findViewById(R.id.swipe);
        mCountriesListView = findViewById(R.id.countries_recycler_view);

        mSwipe.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCountriesListView.setLayoutManager(linearLayoutManager);

        mRequestQueue = Volley.newRequestQueue(this, new HurlStack());

        onRefresh();
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (manager != null)
            networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void setProgress(boolean loading) {
        mSwipe.setRefreshing(loading);
        mCountriesListView.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    private void showError() {
        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show();
    }

    private void retrieveData() {
        String data = readFromFile();
        parseData(data);
    }

    private void fetchData() {
        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);

                // Store data
                writeToFile(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                retrieveData();
                error.printStackTrace();
            }
        });

        mRequestQueue.add(stringRequest);
    }

    private void parseData(String response) {
        if (response == null || response.length() == 0) {
            setProgress(false);
            showError();
            return;
        }

        Gson gson = new Gson();
        Country[] countries = gson.fromJson(response, Country[].class);
        mCountries = Arrays.asList(countries);
        setProgress(false);

        // Show data
        populateListView();
    }

    private void showCountryDetails(Country country) {
        Intent countryIntent = new Intent(this, CountryActivity.class);
        countryIntent.putExtra(CountryActivity.KEY_COUNTRY, country);
        startActivity(countryIntent);
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(openFileOutput(DATA_FILENAME, Context.MODE_PRIVATE));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile() {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            InputStream inputStream = openFileInput(DATA_FILENAME);

            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String s;

                while ((s = bufferedReader.readLine()) != null) {
                    stringBuilder.append(s);
                }

                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return stringBuilder.toString();
    }

    private void populateListView() {
        CountriesAdapter countriesAdapter = new CountriesAdapter(this);
        mCountriesListView.setAdapter(countriesAdapter);
    }

    @Override
    public void onRefresh() {
        setProgress(true);

        if (isConnected())
            fetchData();
        else
            retrieveData();
    }

    private class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountryHolder> {

        private Context mContext;

        CountriesAdapter(Context context) {
            mContext = context;
        }

        @Override
        public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CountryHolder(LayoutInflater.from(mContext).inflate(R.layout.country_city_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(CountryHolder holder, int position) {
            final Country country = mCountries.get(position);
            holder.nameView.setText(country.name);
            holder.indicatorView.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24px);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCountryDetails(country);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCountries != null ? mCountries.size() : 0;
        }

        class CountryHolder extends RecyclerView.ViewHolder {

            TextView nameView;
            ImageView indicatorView;

            CountryHolder(View itemView) {
                super(itemView);

                nameView = itemView.findViewById(R.id.name);
                indicatorView = itemView.findViewById(R.id.indicator);
            }
        }
    }


}
