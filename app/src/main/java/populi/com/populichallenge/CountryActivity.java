package populi.com.populichallenge;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import populi.com.populichallenge.model.City;
import populi.com.populichallenge.model.Country;
import populi.com.populichallenge.model.Tour;

public class CountryActivity extends AppCompatActivity {

    public static final String KEY_COUNTRY = "country";

    private ExpandableListView mCitiesListView;
    private Country country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        mCitiesListView = findViewById(R.id.cities_list);
        country = getIntent().getParcelableExtra(KEY_COUNTRY);

        setTitle(country.name);

        CitiesAdapter citiesAdapter = new CitiesAdapter(this, country.cities);
        mCitiesListView.setAdapter(citiesAdapter);
        mCitiesListView.setGroupIndicator(null);
    }

    private void showTourDetails(Tour tour) {
        Intent tourIntent = new Intent(this, TourActivity.class);
        tourIntent.putExtra(TourActivity.KEY_TOUR, tour);
        startActivity(tourIntent);
    }

    private class CitiesAdapter implements ExpandableListAdapter {

        private Context mContext;
        private List<City> cities;

        CitiesAdapter(Context context, List<City> cityList) {
            mContext = context;
            cities = cityList;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return (cities != null) ? cities.size() : 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return cities.get(groupPosition).tours.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.country_city_cell, parent, false);
            }

            TextView nameView = convertView.findViewById(R.id.name);
            ImageView indicatorView = convertView.findViewById(R.id.indicator);

            nameView.setText(cities.get(groupPosition).name);
            indicatorView.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_up_black_24px : R.drawable.ic_keyboard_arrow_down_black_24px);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.tour_cell, parent, false);
            }

            TextView tourNameView = convertView.findViewById(R.id.tour_title);

            final Tour tour = cities.get(groupPosition).tours.get(childPosition);
            tourNameView.setText(tour.title);
            // download image

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTourDetails(tour);
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }
    }
}
