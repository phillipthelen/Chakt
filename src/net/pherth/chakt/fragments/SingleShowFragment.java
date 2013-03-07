package net.pherth.chakt.fragments;

import static net.pherth.chakt.Reversed.reversed;

import java.util.List;

import net.pherth.chakt.MainActivity_;
import net.pherth.chakt.PreferencesActivity_;
import net.pherth.chakt.R;
import net.pherth.chakt.SingleEpisodeActivity_;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.ShowSeasonsAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.keyboardsurfer.android.widget.crouton.Style;

@EFragment(R.layout.fragment_single_show)
@OptionsMenu(R.menu.activity_single)
public class SingleShowFragment extends SingleBaseFragment {
	
	TvShow show;
	TraktWrapper tw;
	Context cxt;
	
	@Bean
	ShowSeasonsAdapter adapter;
	
	@ViewById
	TextView titletext;
	@ViewById
	TextView airsvalue;
	@ViewById
	TextView runtimevalue;
	@ViewById
	TextView releasevalue;
	@ViewById
	TextView playsvalue;
	@ViewById
	TextView descriptiontext;
	@ViewById
	ImageView headerimage;		
	@ViewById
	TextView ratingsvalue;
	@ViewById
	ProgressBar progressBar;
	@ViewById
	TextView progresstext;
	@ViewById
	TextView seasonvalue;
	@ViewById
	TextView episodevalue;
	@ViewById
	StickyListHeadersListView seasonlist;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_single_show, container, false);
		show= (TvShow) getActivity().getIntent().getExtras().get("show");
		
		cxt = getActivity().getApplicationContext();
		
		LinearLayout mTop = (LinearLayout) inflater.inflate(R.layout.fragment_single_show_details, null);
		ListView seasonlist = (StickyListHeadersListView) view.findViewById(R.id.seasonlist);
		
		adapter.init(getActivity(), show);
		
		seasonlist.addHeaderView(mTop, null, false);
		// Assign adapter to ListView
		seasonlist.setAdapter(adapter); 
		seasonlist.setItemsCanFocus(false);
		
		seasonlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	TvShowEpisode selEpisode =(TvShowEpisode) (parent.getItemAtPosition(position-1));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleEpisodeActivity_.class);
            	recentIntent.putExtra("episode", selEpisode);
            	recentIntent.putExtra("show", show);
                startActivityForResult(recentIntent, 0);
            }
        });
		
		tw = TraktWrapper.getInstance();
		
        return view;
    }
	
	@AfterViews
	void loadData() {
		titletext.setText(show.title);
		if(show.progress == null) {
			displayDetails();
		}
		loadDetails();
	}
	
	@Background
	void loadDetails() {
		setIndeterminateProgress(true);
		try {
			show = tw.showService().summary(show.tvdbId).extended().fire();
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
			setIndeterminateProgress(false);
			return;
		}
		if(show.inWatchlist == null) {
			spawnWrongAuthDialog();
		}
		displayDetails();
		if(show.progress == null) {
			List<TvShow> shows;
			try {
				shows = tw.userService().progressWatched(tw.username).title(show.tvdbId).fire();
			} catch(TraktException e) {
				displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
				setIndeterminateProgress(false);
				return;
			} catch(IndexOutOfBoundsException e) {
				setIndeterminateProgress(false);
				displayEpisodes();
				return;
			}
			if(shows.size() > 0) {
				show.progress = shows.get(0).progress;
			} else {
				setUnseen();
			}
		}
		displayEpisodes();
		setIndeterminateProgress(false);
	}
	
	@UiThread
	@Override
	void spawnWrongAuthDialog() {
		super.spawnWrongAuthDialog();
	}
	
	@UiThread
	void displayDetails() {
		if(show.airDay != null) {
			airsvalue.setText(show.airDay.toString() + " " + show.airTime);
		} else {
			airsvalue.setText(getString(R.string.noair));
		}
		releasevalue.setText(android.text.format.DateFormat.format("dd.MM.yyyy", show.firstAired));
		runtimevalue.setText(show.runtime.toString() + " " + getString(R.string.minutes));
		ratingsvalue.setText(show.ratings.percentage + "%");
		descriptiontext.setText(show.overview);
		
		ImageLoader loader = ImageLoader.getInstance();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.placeholder)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		loader.displayImage(show.images.fanart, headerimage, options);
	}
		
	@UiThread
	void setUnseen() {
		//TODO: Update GUI to tell that show hasn't been watched yet.
	}
	
	@UiThread
	void displayEpisodes() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		if(show.seasons.get(show.seasons.size()-1).season == 0) {
			seasonvalue.setText(String.valueOf(show.seasons.size()-1));
    	} else {
			seasonvalue.setText(String.valueOf(show.seasons.size()));
    	}
		
        episodevalue.setText(String.valueOf(show.progress.aired));
		
		if(!sharedPref.getBoolean("show_specials", true)) {
        	//if the season number is 0, it indeed is the specials "season"
        	if(show.seasons.get(show.seasons.size()-1).season == 0) {
        		show.seasons.remove(show.seasons.size() - 1);
        	}
        }
		for(TvShowSeason season : reversed(show.seasons)) {
			if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
				adapter.addAll(season.episodes.episodes);
			} else {
				for(TvShowEpisode episode : season.episodes.episodes) {
					adapter.add(episode);
				}
			}
		}

        
		seasonlist.requestLayout();
		progressBar.setProgress(show.progress.percentage);
		progresstext.setText(show.progress.completed + "/" + show.progress.aired);
	}
	
	@OptionsItem
	@Background
	void watchlist(MenuItem item) {
		if (show.inWatchlist) {
			displayCrouton(R.string.tryWatchlistRemove, Style.INFO);
		} else {
			displayCrouton(R.string.tryWatchlistAdd, Style.INFO);
		}
		try {
			tw.switchWatchlistShow(show);
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
			return;
		}
		if (show.inWatchlist) {
			displayCrouton(R.string.showRemove, Style.CONFIRM);
			show.inWatchlist = false;
			this.updateMenuIcon(item, R.drawable.ic_watchlist);
		} else {
			displayCrouton(R.string.showAdd, Style.CONFIRM);
			show.inWatchlist = true;
			this.updateMenuIcon(item, R.drawable.ic_unwatchlist);
		}
	}
	
	@OptionsItem
	@Background
	void checkin(MenuItem item) {
		displayCrouton(R.string.tryCheckin, Style.INFO);
		try {
			tw.checkinShow(show);
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
			return;
		}
		displayCrouton(R.string.showCheckin, Style.CONFIRM);
	}
	
	@OptionsItem
	void menu_settings() {
		Intent recentIntent = new Intent(getActivity().getApplicationContext(), PreferencesActivity_.class);
        startActivityForResult(recentIntent, 0);
	}
	
	@OptionsItem
	void home() {
		Intent upIntent = new Intent(getActivity(), MainActivity_.class);
        if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)) {
            // This activity is not part of the application's task, so create a new task
            // with a synthesized back stack.
            TaskStackBuilder.from(getActivity())
                    .addNextIntent(upIntent)
                    .startActivities();
            getActivity().finish();
        } else {
            // This activity is part of the application's task, so simply
            // navigate up to the hierarchical parent activity.
            NavUtils.navigateUpTo(getActivity(), upIntent);
        }
	}
	
}
