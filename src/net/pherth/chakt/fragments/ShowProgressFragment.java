package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleShowActivity_;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.ShowProgressAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.TvShow;

import de.keyboardsurfer.android.widget.crouton.Style;

@EFragment(R.layout.fragment_baselist)
@OptionsMenu(R.menu.activity_main)
public class ShowProgressFragment extends SherlockFragment {

	TraktWrapper tw;
	@ViewById
	ListView list;
	
	ShowProgressAdapter adapter;
	
	@AfterViews
	void loadFragment() {
		adapter = new ShowProgressAdapter(getActivity().getApplicationContext());
		
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
	void getProgress() {
		List<TvShow> shows;
		try {
			shows = (ArrayList<TvShow>) tw.userService().progressWatched(tw.username).fire();
    	} catch (TraktException e) {
    		tw.handleError(e, getActivity());
    		return;
    	}

		notifyDataset(shows);
	}
	
	@UiThread
	void notifyDataset(List<TvShow> shows) {
		adapter.addAll(shows);
		adapter.notifyDataSetChanged();
	}
	
	@OptionsItem
	void refresh(MenuItem item) {
		getProgress();
	}
	
}
