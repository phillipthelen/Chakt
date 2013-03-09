package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.PreferencesActivity_;
import net.pherth.chakt.R;
import net.pherth.chakt.SingleShowActivity_;
import net.pherth.chakt.TraktInterface;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.ShowProgressAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
import com.jakewharton.trakt.entities.TvShow;

import de.keyboardsurfer.android.widget.crouton.Style;

@EFragment(R.layout.fragment_basestickylist)
public class ShowProgressFragment extends TraktFragment implements TraktInterface {

	TraktWrapper tw;
	@ViewById
	ListView list;
	
	@Bean
	ShowProgressAdapter adapter;
	
	@AfterViews
	void loadFragment() {
		adapter.init(getActivity());
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
	
	public static ShowProgressFragment newInstance() {
		ShowProgressFragment f = new ShowProgressFragment_();
		f.setRetainInstance(true);
		return f;
	}
	
	
	@Background
	public void getProgress() {
		setIndeterminateProgress(true);
		List<TvShow> shows;
		try {
			shows = (ArrayList<TvShow>) tw.userService().progressWatched(tw.username).fire();
    	} catch (TraktException e) {
    		displayCrouton(tw.handleError(e, getActivity()), Style.ALERT);
    		setIndeterminateProgress(false);
    		return;
    	}

		notifyDataset(shows);
		setIndeterminateProgress(false);
	}
	
	@UiThread
	void notifyDataset(List<TvShow> shows) {
		adapter.clear();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if(sharedPref.getBoolean("hide_finished_shows", true)) {
			for(TvShow show : shows) {
				if(show.next_episode) {
					adapter.add(show);
				}
				
			}
		} else {
			for(TvShow show : shows) {
				adapter.add(show);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	@OptionsItem
	@UiThread
	void refresh(MenuItem item) {
		getProgress();
	}
	
}
