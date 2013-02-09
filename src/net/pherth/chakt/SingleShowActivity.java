package net.pherth.chakt;

import net.pherth.chakt.fragments.SingleShowFragment_;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.jakewharton.trakt.entities.TvShow;

@EActivity(R.layout.activity_single_show)
@OptionsMenu(R.menu.activity_single)
public class SingleShowActivity extends SherlockFragmentActivity {

	@FragmentById
	SingleShowFragment_ single_show_fragment;
	
	TvShow show;
}
