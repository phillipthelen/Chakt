package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleShowActivity_;
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
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.TvShow;

@EFragment(R.layout.fragment_baselist)
public class SearchFragment extends SherlockFragment {

	TraktWrapper tw;
	@ViewById
	ListView list;
	
	BaselistAdapter adapter;
	
	@AfterViews
	void loadFragment() {
		
		tw = TraktWrapper.getInstance();
		getProgress();
	}
	
	public static SearchFragment newInstance() {
		SearchFragment f = new SearchFragment_();
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
