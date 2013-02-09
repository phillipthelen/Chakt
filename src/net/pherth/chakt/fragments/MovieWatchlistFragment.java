package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleMovieActivity_;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.BaselistAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;

@EFragment(R.layout.fragment_baselist)
public class MovieWatchlistFragment extends SherlockFragment {

	
	TraktWrapper tw;
	
	
	@ViewById
	ListView list;
	
	@Bean
	BaselistAdapter adapter;
	
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
	void getProgress() {
		List<Movie> movies;
		try {
			movies = (ArrayList<Movie>) tw.userService().watchlistMovies(tw.username).fire();
    	} catch (TraktException e) {
    		return;
    	}

		notifyDataset(movies);
	}
	
	@UiThread
	void notifyDataset(List<Movie> movies) {
		adapter.addAll(movies);
		adapter.notifyDataSetChanged();
	}
	
}