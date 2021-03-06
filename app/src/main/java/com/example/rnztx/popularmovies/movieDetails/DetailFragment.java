package com.example.rnztx.popularmovies.movieDetails;


import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rnztx.popularmovies.R;
import com.example.rnztx.popularmovies.data.MovieContract;
import com.example.rnztx.popularmovies.handlers.HttpHandler;
import com.example.rnztx.popularmovies.modules.MovieInfo;
import com.example.rnztx.popularmovies.modules.tmdb.reviews.ReviewResult;
import com.example.rnztx.popularmovies.modules.tmdb.reviews.TmdbReviews;
import com.example.rnztx.popularmovies.modules.tmdb.videos.TmdbVideos;
import com.example.rnztx.popularmovies.modules.tmdb.videos.VideoResult;
import com.example.rnztx.popularmovies.modules.utils.Constants;
import com.example.rnztx.popularmovies.modules.utils.Utilities;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    @Bind(R.id.img_detailActivity_poster) ImageView imgMoviePoster;
    @Bind(R.id.container_movie_trailer) LinearLayout containerMovieTrailer;
    @Bind(R.id.container_movie_review) LinearLayout containerMovieReview;
    @Bind(R.id.txt_movie_review_heading) TextView txtReviewsHeading;
    @Bind(R.id.txt_movie_trailer_heading) TextView txtTrailersHeading;

    private MovieInfo mMovieInfo ;
    private static boolean isSaved = false;
    private MovieDataTask mFetchMovieDataTask;
    private View.OnClickListener mTrailerClickListener;
    private HashMap<ImageView,String> mTrailerKeys = new HashMap<>();
    private MenuItem mMenuItemFavorite;

    final static String IMAGE_BASE = "http://image.tmdb.org/t/p/w185/";
    public DetailFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_view_menu,menu);
        mMenuItemFavorite = menu.findItem(R.id.menu_action_favorite);
        checkFavourite();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu = item.getItemId();
        if (menu == R.id.menu_action_share){
            try {
                // now get first trailer
                String videoKey = mTrailerKeys.values().toArray()[0].toString();
                String youtubeUrl = "http://www.youtube.com/watch?v="+videoKey;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,youtubeUrl);
                intent.setType("text/plain");
                startActivity(intent);
            }catch (ArrayIndexOutOfBoundsException e){
                Toast.makeText(getContext(),"NOT AVAILABLE",Toast.LENGTH_SHORT).show();
            }
        }
        else if(menu == R.id.menu_action_favorite){
            onActionFavoriteToggled();
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateIcon(){
        if (isSaved)
            mMenuItemFavorite.setIcon(R.drawable.ic_favorite_full);
        else
            mMenuItemFavorite.setIcon(R.drawable.ic_favorite_holo);
    }

    public void onActionFavoriteToggled(){
        if (mMovieInfo != null){

            if (isSaved){ // delete from database
                long movieId = Long.valueOf(mMovieInfo.getMovie_id());
                int rowsDeleted = getContext().getContentResolver().delete(
                        MovieContract.MovieEntry.buildMovieWithIdUri(movieId),
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{mMovieInfo.getMovie_id()}
                );
                if (rowsDeleted > 0){
//                    Log.e(LOG_TAG,"Movie Deleted");
                    updateIcon();
                    isSaved = false;
                }else
                    Log.e(LOG_TAG,"Failed to delete" +rowsDeleted);
            }
            else { // save to databse
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry._ID,mMovieInfo.getMovie_id());
                values.put(MovieContract.MovieEntry.COLUMN_TITLE,mMovieInfo.getTitle());
                values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,mMovieInfo.getRelease_date());
                values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,mMovieInfo.getPoster_path());
                values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,mMovieInfo.getVote_avg());
                values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,mMovieInfo.getPlot());

                Uri movieUri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);
                long rowId = ContentUris.parseId(movieUri);
                storePosterToExternalStorage(mMovieInfo.getPoster_path());
                if (rowId>0){
                    isSaved = true;
                }
            }
            updateIcon();
        }
    }

    private void checkFavourite(){
        if (mMovieInfo!=null){
            Cursor cursor = null;
            try {
                long movieId = Long.valueOf(mMovieInfo.getMovie_id());
                cursor = getContext().getContentResolver().query(
                        MovieContract.MovieEntry.buildMovieWithIdUri(movieId),
                        null,
                        MovieContract.MovieEntry._ID+" = "+movieId,null,null);
                if (cursor.moveToFirst()){
                    if (movieId == Long.valueOf(cursor.getString(0))){
                        isSaved = true;
                    }
                }
                else {
                    isSaved = false;
                }
            }catch (Exception e){
                Log.e(LOG_TAG,e.toString());
            }finally {
                if (cursor!=null)
                    cursor.close();
                updateIcon();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //retrieve object in Two Pane
        Bundle arguments = getArguments();
        if (arguments==null) {
            // retrieve object in single pane
            Intent intent = getActivity().getIntent();
            arguments = intent.getExtras();
        }

        mMovieInfo = arguments.getParcelable(Constants.ARG_MOVIE_DETAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,rootView);

        // set title
        TextView txtMovieTitle= (TextView)rootView.findViewById(R.id.txt_movie_title);
        txtMovieTitle.setText(mMovieInfo.getTitle());

        // set Poster Image
        // check if image is stored on sd! offline mode...

        File imagePath = Utilities.getAbsoluteImageStoragePath(mMovieInfo.getPoster_path());
        String imgUrl = IMAGE_BASE+ mMovieInfo.getPoster_path();
        if (imagePath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getPath());
            imgMoviePoster.setImageBitmap(bitmap);
        }else
            Picasso.with(getContext()).load(imgUrl).into(imgMoviePoster);

        // set movie Summary
        TextView txtMovieSummary= (TextView)rootView.findViewById(R.id.txt_movie_summary);
        txtMovieSummary.setText(mMovieInfo.getPlot());

        // set Release Date
        TextView txtMovieReleaseDate= (TextView)rootView.findViewById(R.id.txt_movie_releaseDate);
        txtMovieReleaseDate.setText(mMovieInfo.getRelease_date());

        // set avg rating
        TextView txtMovieRatings= (TextView)rootView.findViewById(R.id.txt_movie_rating);
        txtMovieRatings.setText(mMovieInfo.getVote_avg());

        // download movie reviews and Videos info
        mFetchMovieDataTask = new MovieDataTask(getContext(),mMovieInfo.getMovie_id());
        mFetchMovieDataTask.execute();

        mTrailerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Youtube url play is referred from
                 * http://stackoverflow.com/a/12439378/2804351
                 */
                String youtubeKey = mTrailerKeys.get(v);
                Intent intentYoutubeTrailer;
                try {
                    intentYoutubeTrailer = new Intent(Intent.ACTION_VIEW,Uri.parse("vnd.youtube:"+youtubeKey));
                    startActivity(intentYoutubeTrailer);
                }catch (ActivityNotFoundException e){
                    intentYoutubeTrailer = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v="+youtubeKey));
                    startActivity(intentYoutubeTrailer);
                }
            }
        };
        return rootView;
    }
    public void displayMovieReviews(Context context, TmdbReviews tmdbReviews){
        for (ReviewResult result: tmdbReviews.getResults()){
            TextView txtAuthorName = new TextView(context);
            txtAuthorName.setText(result.getAuthor());

            TextView txtContent = new TextView(context);
            txtContent.setText(result.getContent());
            // make author bold italic
            txtAuthorName.setTypeface(null, Typeface.BOLD_ITALIC);
            txtAuthorName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.movDetail_txtSize_other)
            );
            txtContent.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.movDetail_txtSize_other)
            );
            containerMovieReview.addView(txtAuthorName);
            containerMovieReview.addView(txtContent);
        }
    }

    public void displayMovieVideos(Context context, TmdbVideos tmdbVideos){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,16,0,16);

        for (VideoResult result: tmdbVideos.getResults()){
            String youtubeKey = result.getKey();
            String videoThumbnailUrl = Utilities.getYoutubeVideoThumbnailUrl(youtubeKey);

            ImageView imageView = new ImageView(context);
            imageView.setOnClickListener(mTrailerClickListener);
            imageView.setLayoutParams(layoutParams);
            Picasso.with(getContext()).load(videoThumbnailUrl).into(imageView);
            mTrailerKeys.put(imageView,youtubeKey);
            containerMovieTrailer.addView(imageView);
        }
    }

    private void storePosterToExternalStorage(String imageName){
        // this code snippet is referred from StackOverflow
        if (imgMoviePoster!=null){
            // get image from ImageView
            BitmapDrawable bitmapDrawable = (BitmapDrawable)imgMoviePoster.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            // get absolute image Path
            File imageFile = Utilities.getAbsoluteImageStoragePath(imageName);
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
//                Log.e(LOG_TAG,"Saved to"+imageFile.getPath());
            }catch (Exception e){
                Log.e(LOG_TAG,e.toString());
            }
        }
    }


    /**
     * Created by rnztx on 16/4/16.
     */
    private class MovieDataTask extends AsyncTask<Void,Void,Boolean> {
        private final String LOG_TAG = MovieDataTask.class.getSimpleName();
        private String movie_id;
        private String urlMovieVideos;
        private String urlMovieReviews;
        private TmdbReviews movieReviews;
        private TmdbVideos movieVideos;
        private Context context;
        public MovieDataTask(Context context ,String id){
            this.context = context;
            this.movie_id = id;
            this.urlMovieReviews = Utilities.buildUrlForMovieReviews(movie_id);
            this.urlMovieVideos = Utilities.buildUrlForMovieVideos(movie_id);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandler httpHandler = new HttpHandler();
            try {
                String jsonMoviesReviewData = httpHandler.execute(this.urlMovieReviews);
                String jsonMovieVideosData = httpHandler.execute(this.urlMovieVideos);
                movieReviews = new Gson().fromJson(jsonMoviesReviewData,TmdbReviews.class);
                movieVideos = new Gson().fromJson(jsonMovieVideosData,TmdbVideos.class);
                return true;
            }catch (Exception e){
                Log.e(LOG_TAG,e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                displayMovieVideos(this.context,movieVideos);
                displayMovieReviews(this.context,movieReviews);
//                Log.e(LOG_TAG,"No of reviews: "+movieReviews.getResults().size());
                //enable headings

                txtTrailersHeading.setVisibility(View.VISIBLE);
                txtReviewsHeading.setVisibility(View.VISIBLE);
            }
        }
    }
}
