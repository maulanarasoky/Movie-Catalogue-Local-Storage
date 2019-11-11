package com.example.moviecataloguelocalstorage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviecataloguelocalstorage.R;
import com.example.moviecataloguelocalstorage.database.OperationHelper;
import com.example.moviecataloguelocalstorage.model.ListData;
import com.example.moviecataloguelocalstorage.model.ListDataFavoriteMovie;

public class DetailsActivityMovie extends AppCompatActivity implements View.OnClickListener {

    public static final String SEND_MOVIE = "send_movie";

    public static final String EXTRA_MOVIE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private ListDataFavoriteMovie movieFav;
    private int position;

    private static boolean isInsert = false;

    private OperationHelper movieFavHelper;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;

    TextView tvTitleMovFav;
    TextView tvReleaseDateMovFav;
    TextView tvVoteAverageMovFav;
    TextView tvOverviewMovFav;
    TextView tvUrlMovFav;
    ImageView imageView;
    Button btnAddMov;

    ListData movies;

    int movFavId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        tvTitleMovFav = findViewById(R.id.title);
        tvReleaseDateMovFav = findViewById(R.id.release_date);
        tvVoteAverageMovFav = findViewById(R.id.vote);
        tvOverviewMovFav = findViewById(R.id.description);
        imageView = findViewById(R.id.img_item_photo);
        tvUrlMovFav = findViewById(R.id.tv_url_image_mov);

        btnAddMov = findViewById(R.id.btn_add_favorite);
        btnAddMov.setOnClickListener(this);

        movieFavHelper = OperationHelper.getInstance(getApplicationContext());
        movieFavHelper.open();

        movieFav = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movieFav != null){
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isInsert  = true;
            btnAddMov.setVisibility(View.GONE);
        } else {
            movieFav = new ListDataFavoriteMovie();
        }

        movies = getIntent().getParcelableExtra(SEND_MOVIE);

        if (movies != null) {
            tvTitleMovFav.setText(movies.getTitle());
            tvReleaseDateMovFav.setText(movies.getReleaseDate());
            tvVoteAverageMovFav.setText(movies.getRatings());
            tvOverviewMovFav.setText(movies.getDescription());
            tvUrlMovFav.setText(movies.getPhoto());

            Glide.with(DetailsActivityMovie.this)
                    .load(movies.getPhoto())
                    .placeholder(R.color.colorAccent)
                    .override(50, 75)
                    .into(imageView);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_favorite) {

            String titles = tvTitleMovFav.getText().toString().trim();
            String overview = tvOverviewMovFav.getText().toString().trim();
            String release_date = tvReleaseDateMovFav.getText().toString().trim();
            String vote_average = tvVoteAverageMovFav.getText().toString().trim();

            String url_poster = tvUrlMovFav.getText().toString().trim();

            movieFav.setId(movies.getId());
            movieFav.setTitle(titles);
            movieFav.setDescription(overview);
            movieFav.setReleaseDate(release_date);
            movieFav.setRatings(vote_average);
            movieFav.setPhoto(url_poster);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE, movieFav);
            intent.putExtra(EXTRA_POSITION, position);

            if (!isInsert) {

                long result = movieFavHelper.insertMovie(movieFav);

                if (result > 0) {
                    movieFav.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    Toast.makeText(DetailsActivityMovie.this, getString(R.string.success_add), Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Toast.makeText(DetailsActivityMovie.this, getString(R.string.failed_add), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

        }
    }
}
