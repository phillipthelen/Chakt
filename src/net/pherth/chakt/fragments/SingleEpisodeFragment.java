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
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


@EFragment(R.layout.fragment_single_episode)
public class SingleEpisodeFragment extends SingleBaseFragment {
	
	TvShowEpisode episode;
	
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
		episode = (TvShowEpisode) getActivity().getIntent().getExtras().get("episode");
        return null;
    }
	
	@AfterViews
	void loadData() {
		titletext.setText(episode.season + "x" + episode.number + " " + episode.title);
		releasevalue.setText(android.text.format.DateFormat.format("dd.MM.yyyy", episode.firstAired));
		ratingsvalue.setText(episode.ratings.percentage + "% (" + episode.ratings.votes + " " + getString(R.string.votes) + ")");
		if(episode.watched) {
			playsvalue.setText(episode.plays.toString() + " " + getString(R.string.times));
		} else {
			playsvalue.setText(R.string.notplayed);
		}
		descriptiontext.setText(episode.overview);
		
		ImageLoader loader = ImageLoader.getInstance();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.placeholder)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		loader.displayImage(episode.images.screen, headerimage, options);
	}
	
}
