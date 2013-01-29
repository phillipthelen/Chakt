package net.pherth.chakt.fragments;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.R.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;

@EFragment
public class ShowProgressFragment extends SherlockFragment {

	TraktWrapper tw;
	
	
	@ViewById
	ListView progresslist;
	
	@AfterViews
	void loadFragment() {

		// Assign adapter to ListView
		//progresslist.setAdapter(adapter); 
		
		tw = TraktWrapper.getInstance();
		getProgress();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showprogress, container, false);    }
	
	public static ShowProgressFragment newInstance() {
		ShowProgressFragment f = new ShowProgressFragment();
		return f;
	}
	
	@Background
	void getProgress() {
		List<TvShow> shows;
		try {
    	} catch (TraktException e) {
    		return;
    	}
	}
	
}
