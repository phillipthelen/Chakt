package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.fragments.EpisodeWatchlistFragment_;
import net.pherth.chakt.R;
import net.pherth.chakt.SingleMovieActivity_;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.R.layout;
import net.pherth.chakt.adapter.BaselistAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.viewpagerindicator.TitlePageIndicator;

@EFragment(R.layout.fragment_baselist)
public class EpisodeWatchlistFragment extends SherlockFragment {

	TraktWrapper tw;
	@ViewById
	ListView list;
	
	BaselistAdapter adapter;
	
	@AfterViews
	void loadFragment() {
		adapter = new BaselistAdapter(getActivity().getApplicationContext());
		
		// Assign adapter to ListView
		list.setAdapter(adapter); 
		list.setItemsCanFocus(false);
		
		list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	TvShowEpisode selEpisode =(TvShowEpisode) (parent.getItemAtPosition(position));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleMovieActivity_.class);
            	recentIntent.putExtra("episode", selEpisode);
                startActivityForResult(recentIntent, 0);
            }
        });
		
		tw = TraktWrapper.getInstance();
		getProgress();
	}
	
	public static EpisodeWatchlistFragment newInstance() {
		EpisodeWatchlistFragment f = new EpisodeWatchlistFragment_();
		f.setRetainInstance(true);
		return f;
	}
	
	
	@Background
	void getProgress() {
		List<TvShow> episodes;
		try {
			episodes = (ArrayList<TvShow>) tw.userService().watchlistEpisodes(tw.username).fire();
    	} catch (TraktException e) {
    		return;
    	}

		notifyDataset(episodes);
	}
	
	@UiThread
	void notifyDataset(List<TvShow> episodes) {
		adapter.addAll(episodes);
		adapter.notifyDataSetChanged();
	}
	
}
