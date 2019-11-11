package com.example.moviecataloguelocalstorage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moviecataloguelocalstorage.model.ListDataFavoriteMovie;
import com.example.moviecataloguelocalstorage.model.ListDataFavoriteTv;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class OperationHelper {

    private DatabaseHelper dataBaseHelper;
    private static OperationHelper INSTANCE;

    private SQLiteDatabase database;

    public OperationHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public static OperationHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OperationHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public ArrayList<ListDataFavoriteMovie> getAllMoviesFav() {
        ArrayList<ListDataFavoriteMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseContract.MovieColumns.TABLE_FAVORITES, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        ListDataFavoriteMovie movieFavorite;
        if (cursor.getCount() > 0) {
            do {
                movieFavorite = new ListDataFavoriteMovie();
                movieFavorite.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieFavorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE)));
                movieFavorite.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION)));
                movieFavorite.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE)));
                movieFavorite.setRatings(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTES)));
                movieFavorite.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH)));

                arrayList.add(movieFavorite);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<ListDataFavoriteTv> getAllTvFav() {
        ArrayList<ListDataFavoriteTv> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseContract.TvColumns.TABLE_FAVORITES, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        ListDataFavoriteTv listDataFavoriteTv;
        if (cursor.getCount() > 0) {
            do {
                listDataFavoriteTv = new ListDataFavoriteTv();
                listDataFavoriteTv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                listDataFavoriteTv.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.TITLE)));
                listDataFavoriteTv.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.DESCRIPTION)));
                listDataFavoriteTv.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.RELEASE_DATE)));
                listDataFavoriteTv.setRatings(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.VOTES)));
                listDataFavoriteTv.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.POSTER_PATH)));

                arrayList.add(listDataFavoriteTv);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertMovie(ListDataFavoriteMovie listDataFavoriteMovie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(_ID, listDataFavoriteMovie.getId());
        initialValues.put(DatabaseContract.MovieColumns.TITLE, listDataFavoriteMovie.getTitle());
        initialValues.put(DatabaseContract.MovieColumns.DESCRIPTION, listDataFavoriteMovie.getDescription());
        initialValues.put(DatabaseContract.MovieColumns.VOTES, listDataFavoriteMovie.getRatings());
        initialValues.put(DatabaseContract.MovieColumns.RELEASE_DATE, listDataFavoriteMovie.getReleaseDate());
        initialValues.put(DatabaseContract.MovieColumns.POSTER_PATH, listDataFavoriteMovie.getPhoto());
        return database.insert(DatabaseContract.MovieColumns.TABLE_FAVORITES, null, initialValues);
    }

    public long insertTv(ListDataFavoriteTv listDataFavoriteTv) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(_ID, listDataFavoriteTv.getId());
        initialValues.put(DatabaseContract.TvColumns.TITLE, listDataFavoriteTv.getTitle());
        initialValues.put(DatabaseContract.TvColumns.DESCRIPTION, listDataFavoriteTv.getDescription());
        initialValues.put(DatabaseContract.TvColumns.VOTES, listDataFavoriteTv.getRatings());
        initialValues.put(DatabaseContract.TvColumns.RELEASE_DATE, listDataFavoriteTv.getReleaseDate());
        initialValues.put(DatabaseContract.TvColumns.POSTER_PATH, listDataFavoriteTv.getPhoto());
        return database.insert(DatabaseContract.TvColumns.TABLE_FAVORITES, null, initialValues);
    }

    public int deleteMovie(int id) {
        return database.delete(DatabaseContract.MovieColumns.TABLE_FAVORITES, _ID + " = '" + id + "'", null);
    }

    public int deleteTv(int id) {
        return database.delete(DatabaseContract.TvColumns.TABLE_FAVORITES, _ID + " = '" + id + "'", null);
    }
}
