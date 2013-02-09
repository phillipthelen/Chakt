package net.pherth.chakt;

import net.pherth.chakt.fragments.SingleMovieFragment_;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.jakewharton.trakt.entities.Movie;

@EActivity(R.layout.activity_single)
public class SingleMovieActivity extends SherlockFragmentActivity {

	@FragmentById
	SingleMovieFragment_ single_movie_fragment;
	
	Movie movie;

}
