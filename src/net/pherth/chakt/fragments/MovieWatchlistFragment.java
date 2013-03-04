package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.PreferencesActivity_;
import net.pherth.chakt.R;
import net.pherth.chakt.SingleMovieActivity_;
import net.pherth.chakt.TraktInterface;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.BaseStickylistAdapter;
import net.pherth.chakt.adapter.BaselistAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;

import de.keyboardsurfer.android.widget.crouton.Style;

@EFragment(R.layout.fragment_basestickylist)
public class MovieWatchlistFragment extends TraktFragment implements TraktInterface {

	
	TraktWrapper tw;
	
	
	@ViewById
	ListView list;
	
	@Bean
	BaseStickylistAdapter adapter;
	
	@AfterViews
	void loadFragment() {
		
		adapter.init(getActivity(), "movie");
		
		// Assign adapter to ListView
		list.setAdapter(adapter); 
		list.setItemsCanFocus(false);
		
		list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Movie selMovie =(Movie) (parent.getItemAtPosition(position));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleMovieActivity_.class);
            	recentIntent.putExtra("movie", selMovie);
                startActivityForResult(recentIntent, 0);
            }
        });
		
		tw = TraktWrapper.getInstance();
		getProgress();
	}
	
	public static MovieWatchlistFragment newInstance() {
		MovieWatchlistFragment f = new MovieWatchlistFragment_();
		f.setRetainInstance(true);
		return f;
	}
	
	
	@Background
	public void getProgress() {
		setIndeterminateProgress(true);
		List<Movie> movies;
		try {
			movies = (ArrayList<Movie>) tw.userService().watchlistMovies(tw.username).fire();
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
			setIndeterminateProgress(false);
			return;
		}

		notifyDataset(movies);
		setIndeterminateProgress(false);
	}
	
	@UiThread
	void notifyDataset(List<Movie> movies) {
		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
			adapter.addAll(movies);
		} else {
			for(Movie movie : movies) {
				adapter.add(movie);
			}
		}
		adapter.notifyDataSetChanged();
	}
}