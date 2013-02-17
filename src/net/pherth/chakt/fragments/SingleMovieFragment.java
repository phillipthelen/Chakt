package net.pherth.chakt.fragments;

import net.pherth.chakt.PreferencesActivity_;
import net.pherth.chakt.R;
import net.pherth.chakt.SingleMovieActivity_;
import net.pherth.chakt.TraktWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
		if(movie.watched == null) {
			spawnWrongAuthDialog();
		} else {
			if(movie.watched) {
				playsvalue.setText(movie.plays.toString() + " " + getString(R.string.times));
			} else {
				playsvalue.setText(R.string.notplayed);
			}
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
			tw.switchWatchlistMovie(movie);
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
			return;
		}
		if (movie.inWatchlist) {
			displayCrouton(R.string.movieRemove, Style.CONFIRM);
			movie.inWatchlist = false;
			this.updateMenuIcon(item, R.drawable.ic_watchlist);
		} else {
			displayCrouton(R.string.movieAdd, Style.CONFIRM);
			movie.inWatchlist = true;
			this.updateMenuIcon(item, R.drawable.ic_unwatchlist);
		}
	}
	
	@OptionsItem
	@Background
	void checkin(MenuItem item) {
		try {
			tw.checkinMovie(movie);
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
			return;
		}
		displayCrouton(R.string.movieCheckin, Style.CONFIRM);
		item.setEnabled(false);
	}
	
	@OptionsItem
	void menu_settings() {
		Intent recentIntent = new Intent(getActivity().getApplicationContext(), PreferencesActivity_.class);
        startActivityForResult(recentIntent, 0);
	}
}
