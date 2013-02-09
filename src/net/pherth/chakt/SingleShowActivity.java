package net.pherth.chakt;

import net.pherth.chakt.fragments.SingleShowFragment_;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
    }
}
