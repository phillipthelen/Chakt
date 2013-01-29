package net.pherth.chakt;

import net.pherth.chakt.fragments.SingleEpisodeFragment_;
import net.pherth.chakt.fragments.SingleMovieFragment_;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import com.googlecode.androidannotations.annotations.EActivity;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShowEpisode;

@EActivity(R.layout.activity_single_episode)
@OptionsMenu(R.menu.activity_single)
public class SingleEpisodeActivity extends SherlockFragmentActivity {

	@FragmentById
	SingleEpisodeFragment_ single_episode_fragment;
	
	TvShowEpisode episode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 SingleEpisodeFragment_ single_episode_fragment = new SingleEpisodeFragment_();
         episode = (TvShowEpisode) getIntent().getExtras().get("episode");
         getSupportFragmentManager().beginTransaction().add(R.id.single_episode_fragment, single_episode_fragment).commit();
	}

}
