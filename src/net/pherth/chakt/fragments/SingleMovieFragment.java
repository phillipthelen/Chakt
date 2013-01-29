package net.pherth.chakt.fragments;

import java.text.SimpleDateFormat;

import net.pherth.chakt.R;
import net.pherth.chakt.R.layout;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.entities.Movie;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


@EFragment(R.layout.fragment_single_movie)
public class SingleMovieFragment extends SherlockFragment {
	
	Movie movie;
	
	@ViewById
	TextView titletext;
	@ViewById
	TextView releasevalue;
	@ViewById
	TextView runtimevalue;
	@ViewById
	TextView playsvalue;
	@ViewById
	TextView descriptiontext;
	@ViewById
	ImageView headerimage;
	@ViewById
	TextView ratingsvalue;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		movie = (Movie) getActivity().getIntent().getExtras().get("movie");
        return null;
    }
	
	@AfterViews
	void loadData() {
		
		titletext.setText(movie.title);
		releasevalue.setText(android.text.format.DateFormat.format("dd.MM.yyyy", movie.released));
		runtimevalue.setText(movie.runtime.toString() + " " + getString(R.string.minutes));
		ratingsvalue.setText(movie.ratings.percentage + "% (" + movie.ratings.votes + " " + getString(R.string.votes) + ")");
		if(movie.watched) {
			playsvalue.setText(movie.plays.toString() + " " + getString(R.string.times));
		} else {
			playsvalue.setText(R.string.notplayed);
		}
		descriptiontext.setText(movie.overview);
		
		ImageLoader loader = ImageLoader.getInstance();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.placeholder)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		Log.d("SingleMovieFragment", movie.images.fanart);
		loader.displayImage(movie.images.fanart, headerimage, options);
	}
	
}
