package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleMovieActivity_;
import net.pherth.chakt.SingleShowActivity_;
import net.pherth.chakt.fragments.ShowWatchlistFragment_;
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
import com.viewpagerindicator.TitlePageIndicator;

@EFragment(R.layout.fragment_baselist)
public class ShowWatchlistFragment extends SherlockFragment {

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
            	TvShow selShow =(TvShow) (parent.getItemAtPosition(position));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleShowActivity_.class);
            	recentIntent.putExtra("show", selShow);
                startActivityForResult(recentIntent, 0);
            }
        });
		
		tw = TraktWrapper.getInstance();
		getProgress();
	}
	
	public static ShowWatchlistFragment newInstance() {
		ShowWatchlistFragment f = new ShowWatchlistFragment_();
		f.setRetainInstance(true);
		return f;
	}
	
	
	@Background
	void getProgress() {
		List<TvShow> shows;
		try {
			shows = (ArrayList<TvShow>) tw.userService().watchlistShows(tw.username).fire();
    	} catch (TraktException e) {
    		return;
    	}

		notifyDataset(shows);
	}
	
	@UiThread
	void notifyDataset(List<TvShow> shows) {
		adapter.addAll(shows);
		adapter.notifyDataSetChanged();
	}
	
}
