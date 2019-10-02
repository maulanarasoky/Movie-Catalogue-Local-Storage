package com.example.moviecataloguelocalstorage.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviecataloguelocalstorage.R;
import com.example.moviecataloguelocalstorage.database.OperationHelper;
import com.example.moviecataloguelocalstorage.model.ListDataFavoriteMovie;

public class DetailsMovieFavoriteActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String SEND_MOVIE_FAVORITE = "send_movie_favorite";
    public static final String SEND_POSITION = "send_position";

    TextView tvId;
    TextView tvTitleMovFav;
    TextView tvReleaseDateMovFav;
    TextView tvVoteAverageMovFav;
    TextView tvOverviewMovFav;
    ImageView imageView;
    Button btnDeleteMovFav;

    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private ListDataFavoriteMovie movieFav;
    private int position;

    private OperationHelper movieFavHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie_favorite);

        tvTitleMovFav = findViewById(R.id.title);
        tvReleaseDateMovFav = findViewById(R.id.release_date);
        tvVoteAverageMovFav = findViewById(R.id.vote);
        tvOverviewMovFav = findViewById(R.id.description);
        imageView = findViewById(R.id.img_item_photo);

        btnDeleteMovFav = findViewById(R.id.btn_remove_favorite);
        btnDeleteMovFav.setOnClickListener(this);

        movieFavHelper = OperationHelper.getInstance(getApplicationContext());

        movieFav = getIntent().getParcelableExtra(SEND_MOVIE_FAVORITE);

        if (movieFav != null) {
            position = getIntent().getIntExtra(SEND_POSITION, 0);
            tvTitleMovFav.setText(movieFav.getTitle());
            tvReleaseDateMovFav.setText(movieFav.getReleaseDate());
            tvVoteAverageMovFav.setText(movieFav.getRatings());
            tvOverviewMovFav.setText(movieFav.getDescription());

            Glide.with(DetailsMovieFavoriteActivity.this)
                    .load(movieFav.getPhoto())
                    .placeholder(R.color.colorAccent)
                    .override(50, 75)
                    .into(imageView);

        }
    }


    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (!isDialogClose) {
            dialogMessage = getString(R.string.notify_ques_delete);
            dialogTitle = getString(R.string.notify_delete_mov);

            alertDialogBuilder.setTitle(dialogTitle);
            alertDialogBuilder
                    .setMessage(dialogMessage)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            long result = movieFavHelper.deleteMovie(movieFav.getId());
                            if (result > 0) {
                                Intent intent = new Intent();
                                intent.putExtra(SEND_POSITION, position);
                                setResult(RESULT_DELETE, intent);
                                finish();
                            } else {
                                Toast.makeText(DetailsMovieFavoriteActivity.this, getString(R.string.notify_failed_delete_data), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_remove_favorite) {
            showAlertDialog(ALERT_DIALOG_DELETE);
        }

    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

}
