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
import com.example.moviecataloguelocalstorage.model.ListDataFavoriteTv;

public class DetailsActivityTvShow extends AppCompatActivity implements View.OnClickListener {

    public static final String SEND_MOVIE = "send_movie";

    TextView tvTitleMovFav;
    TextView tvReleaseDateMovFav;
    TextView tvVoteAverageMovFav;
    TextView tvOverviewMovFav;
    TextView tvUrlMovFav;
    ImageView imageView;
    Button btnAddMov;


    public static final String EXTRA_MOVIE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private ListDataFavoriteTv tvFav;
    private int position;

    private OperationHelper movieFavHelper;
    private boolean isInsert = false;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;

    ListData tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tv_show);

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

        tvFav = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (tvFav != null){
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isInsert  = true;
            btnAddMov.setVisibility(View.GONE);
        } else {
            tvFav = new ListDataFavoriteTv();
        }

        tvShow = getIntent().getParcelableExtra(SEND_MOVIE);

        if (tvShow != null) {
            tvTitleMovFav.setText(tvShow.getTitle());
            tvReleaseDateMovFav.setText(tvShow.getReleaseDate());
            tvVoteAverageMovFav.setText(tvShow.getRatings());
            tvOverviewMovFav.setText(tvShow.getDescription());
            tvUrlMovFav.setText(tvShow.getPhoto());

            Glide.with(DetailsActivityTvShow.this)
                    .load(tvShow.getPhoto())
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

            tvFav.setId(tvShow.getId());
            tvFav.setTitle(titles);
            tvFav.setDescription(overview);
            tvFav.setReleaseDate(release_date);
            tvFav.setRatings(vote_average);
            tvFav.setPhoto(url_poster);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE, tvFav);
            intent.putExtra(EXTRA_POSITION, position);

            if (!isInsert) {

                long result = movieFavHelper.insertTv(tvFav);

                if (result > 0) {
                    tvFav.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    Toast.makeText(DetailsActivityTvShow.this, getString(R.string.success_add), Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Toast.makeText(DetailsActivityTvShow.this, getString(R.string.failed_add), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

        }
    }
}
