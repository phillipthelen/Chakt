package net.pherth.chakt;

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

@EActivity(R.layout.activity_single)
@OptionsMenu(R.menu.activity_single)
public class SingleMovieActivity extends SherlockFragmentActivity {

	@FragmentById
	SingleMovieFragment_ single_movie_fragment;
	
	Movie movie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 SingleMovieFragment_ single_movie_fragment = new SingleMovieFragment_();
         movie = (Movie) getIntent().getExtras().get("movie");
         getSupportFragmentManager().beginTransaction().add(R.id.single_movie_fragment, single_movie_fragment).commit();
	}

}
