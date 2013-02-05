package net.pherth.chakt.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.pherth.chakt.R;
import net.pherth.chakt.TraktWrapper;
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
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.services.MovieService.UnwatchlistBuilder;
import com.jakewharton.trakt.services.MovieService.WatchlistBuilder;
import com.jakewharton.trakt.services.ShoutService.MovieBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


@EFragment(R.layout.fragment_single_movie)
@OptionsMenu(R.menu.activity_single)
public class SingleMovieFragment extends SingleBaseFragment {
	
	Movie movie;
	TraktWrapper tw;
	
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
		tw = TraktWrapper.getInstance();
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
	
	@OptionsItem
	@Background
	void watchlist(MenuItem item) {
		try {
			if (movie.inWatchlist) {
				tw.movieService().unwatchlist().movie(movie.imdbId).fire();
				this.showCrouton("The movie was removed", Style.CONFIRM);
				movie.inWatchlist = false;
				this.updateMenuIcon(item, R.drawable.ic_watchlist);
			} else {
				tw.movieService().watchlist().movie(movie.imdbId).fire();
				this.showCrouton("The movie was added", Style.CONFIRM);
				movie.inWatchlist = true;
				this.updateMenuIcon(item, R.drawable.ic_unwatchlist);
			}
    	} catch (TraktException e) {
    		Log.e("ERROR", e.getResponse().error);
    		this.networkErrorCrouton();
    		return;
    	}
		
	}
	
}
