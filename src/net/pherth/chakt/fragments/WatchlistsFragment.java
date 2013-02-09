package net.pherth.chakt.fragments;

import net.pherth.chakt.R;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.WatchlistsFragmentAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.viewpagerindicator.TitlePageIndicator;

@EFragment(R.layout.fragment_watchlists)
public class WatchlistsFragment extends SherlockFragment {

	
	TraktWrapper tw;
	
	FragmentManager fm;
	
	@ViewById
	ListView list;
	
	@ViewById 
	ViewPager pager;
	
	@ViewById
	TitlePageIndicator indicator;
	
	WatchlistsFragmentAdapter adapter;
	
	@AfterViews
	void loadFragment() {
        adapter = new WatchlistsFragmentAdapter(fm);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
	}
	
	public static WatchlistsFragment newInstance(FragmentManager fm) {
		WatchlistsFragment f = new WatchlistsFragment_();
		f.fm = fm;
		f.setRetainInstance(true);
		return f;
	}
	
}