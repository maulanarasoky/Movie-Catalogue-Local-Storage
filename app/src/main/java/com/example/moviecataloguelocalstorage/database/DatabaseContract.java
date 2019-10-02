package com.example.moviecataloguelocalstorage.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static final class MovieColumns implements BaseColumns {
        static final String TABLE_FAVORITES = "moviefav";
        static final String TITLE = "title";
        static final String DESCRIPTION = "description";
        static final String VOTES = "votes";
        static final String RELEASE_DATE = "release_date";
        static final String POSTER_PATH = "poster_path";
    }

    static final class TvColumns implements BaseColumns {
        static final String TABLE_FAVORITES = "tvfav";
        static final String TITLE = "title";
        static final String DESCRIPTION = "description";
        static final String VOTES = "votes";
        static final String RELEASE_DATE = "release_date";
        static final String POSTER_PATH = "poster_path";
    }
}
