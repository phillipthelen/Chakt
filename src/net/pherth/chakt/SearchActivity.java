
package net.pherth.chakt;

import java.util.ArrayList;
import java.util.List;

import net.pherth.chakt.fragments.EpisodeWatchlistFragment_;
import net.pherth.chakt.fragments.MovieWatchlistFragment;
import net.pherth.chakt.fragments.MovieWatchlistFragment_;
import net.pherth.chakt.fragments.ShowProgressFragment;
import net.pherth.chakt.fragments.ShowProgressFragment_;
import net.pherth.chakt.fragments.ShowWatchlistFragment_;
import net.pherth.chakt.fragments.WatchlistsFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EActivity(R.layout.activity_main)
public class SearchActivity
    extends SherlockFragmentActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      Crouton.showText(this, query, Style.ALERT);
	      
	    }
	}
}
