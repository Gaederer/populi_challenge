package populi.com.populichallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import populi.com.populichallenge.model.Tour;

public class TourActivity extends AppCompatActivity {

    public static final String KEY_TOUR = "tour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        Intent intent = getIntent();
        Tour tour = intent.getParcelableExtra(KEY_TOUR);

        setTitle(tour.title);

        NetworkImageView imageView = findViewById(R.id.tour_image);
        TextView contentView = findViewById(R.id.tour_description);

        contentView.setText(tour.desc);
        // download image
    }
}
