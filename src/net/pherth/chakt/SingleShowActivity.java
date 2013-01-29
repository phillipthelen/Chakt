package net.pherth.chakt;

import net.pherth.chakt.fragments.SingleMovieFragment_;
import net.pherth.chakt.fragments.SingleShowFragment_;

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
import com.jakewharton.trakt.entities.TvShow;

@EActivity(R.layout.activity_single_show)
@OptionsMenu(R.menu.activity_single)
public class SingleShowActivity extends SherlockFragmentActivity {

	@FragmentById
	SingleShowFragment_ single_show_fragment;
	
	TvShow show;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 SingleShowFragment_ single_show_fragment = new SingleShowFragment_();
         show = (TvShow) getIntent().getExtras().get("show");
         getSupportFragmentManager().beginTransaction().add(R.id.single_show_fragment, single_show_fragment).commit();
	}

}
