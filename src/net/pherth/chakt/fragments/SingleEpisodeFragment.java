package net.pherth.chakt.fragments;

import java.text.SimpleDateFormat;

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
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvEntity;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.keyboardsurfer.android.widget.crouton.Style;


@EFragment(R.layout.fragment_single_episode)
public class SingleEpisodeFragment extends SingleBaseFragment {
	
	TvShow show;
	TvShowEpisode episode;
	TraktWrapper tw;
	
	@ViewById
	TextView titletext;
	@ViewById
	TextView showvalue;
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
		show = (TvShow) getActivity().getIntent().getExtras().get("show");
		episode = (TvShowEpisode) getActivity().getIntent().getExtras().get("episode");
		tw = TraktWrapper.getInstance();
        return null;
    }
	
	@AfterViews
	void loadData() {
		titletext.setText(episode.season + "x" + episode.number + " " + episode.title);
		showvalue.setText(show.title);
		releasevalue.setText(android.text.format.DateFormat.format("dd.MM.yyyy", episode.firstAired));
		runtimevalue.setText(String.valueOf(show.runtime));
		ratingsvalue.setText(episode.ratings.percentage + "% (" + episode.ratings.votes + " " + getString(R.string.votes) + ")");
		descriptiontext.setText(episode.overview);
		
		ImageLoader loader = ImageLoader.getInstance();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.placeholder)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		loader.displayImage(show.images.fanart, headerimage, options);
		
		loadDetails();
	}
	
	@Background
	void loadDetails() {
		TvEntity entity = tw.showService().episodeSummary(Integer.parseInt(show.tvdbId), episode.season, episode.number).fire();
		episode = entity.episode;
		displayDetails();
	}
	
	@UiThread
	void displayDetails() {
		if(episode.watched) {
			playsvalue.setText(episode.plays.toString() + " " + getString(R.string.times));
		} else {
			playsvalue.setText(R.string.notplayed);
		}
	}
	
	@OptionsItem
	@Background
	void watchlist(MenuItem item) {
		tw.switchWatchlistEpisode(show, episode);
		if (episode.inWatchlist) {
			displayCrouton(R.string.episodeRemove, Style.CONFIRM);
			episode.inWatchlist = false;
			this.updateMenuIcon(item, R.drawable.ic_watchlist);
		} else {
			displayCrouton(R.string.episodeAdd, Style.CONFIRM);
			episode.inWatchlist = true;
			this.updateMenuIcon(item, R.drawable.ic_unwatchlist);
		}
	}
	
	@OptionsItem
	@Background
	void checkin(MenuItem item) {
		tw.checkinEpisode(show, episode);
		displayCrouton(R.string.episodeCheckin, Style.CONFIRM);
		item.setEnabled(false);
	}
	
}
