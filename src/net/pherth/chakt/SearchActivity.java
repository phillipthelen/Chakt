
package net.pherth.chakt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.pherth.chakt.fragments.EpisodeWatchlistFragment_;
import net.pherth.chakt.fragments.MovieWatchlistFragment;
import net.pherth.chakt.fragments.MovieWatchlistFragment_;
import net.pherth.chakt.fragments.SearchEpisodeFragment_;
import net.pherth.chakt.fragments.SearchMovieFragment;
import net.pherth.chakt.fragments.SearchMovieFragment_;
import net.pherth.chakt.fragments.SearchShowFragment_;
import net.pherth.chakt.fragments.ShowProgressFragment;
import net.pherth.chakt.fragments.ShowProgressFragment_;
import net.pherth.chakt.fragments.ShowWatchlistFragment_;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvEntity;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EActivity(R.layout.activity_search)
public class SearchActivity
    extends SherlockFragmentActivity {
	
    private int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;	
    
    TabHost mTabHost;
    TabManager mTabManager;

    
    TraktWrapper tw;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);


	    tw = TraktWrapper.getInstance();
	    
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      this.getSupportActionBar().setTitle(query);
	      searchMovies(query);
	      searchShows(query);
	      searchEpisodes(query);
	    }
	    
	}
	
	@AfterViews
	public void loadFragments() {
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabManager.addTab(mTabHost.newTabSpec("movies").setIndicator("Movies"),
                SearchMovieFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("shows").setIndicator("Shows"),
                SearchMovieFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec("episodes").setIndicator("Episodes"),
                SearchMovieFragment.class, null);
        
	}

	@Background
	public void searchEpisodes(String query) {
		List<TvEntity> episodes;
		try {
			episodes = (ArrayList<TvEntity>) tw.searchService().episodes(query).fire();
		} catch (TraktException e) {
			Crouton.showText(this, tw.handleError(e, this), Style.ALERT);
			return;
		}
		System.out.println(episodes);
		mTabManager.setEpisodes(episodes);
		
	}
	
	@Background
	public void searchShows(String query) {
		List<TvShow> shows;
		try {
			shows = (ArrayList<TvShow>) tw.searchService().shows(query).fire();
		} catch (TraktException e) {
			Crouton.showText(this, tw.handleError(e, this), Style.ALERT);
			return;
		}
		System.out.println(shows);
		mTabManager.setShows(shows);
		
	}
	
	@Background
	public void searchMovies(String query) {
		
		List<Movie> movies;
		try {
			movies = (ArrayList<Movie>) tw.searchService().movies(query).fire();
		} catch (TraktException e) {
			Crouton.showText(this, tw.handleError(e, this), Style.ALERT);
			return;
		}
		System.out.println(movies);
		mTabManager.setMovies(movies);
	}

    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private SearchMovieFragment_ moviefragment;
        private SearchShowFragment_ showfragment;
        private SearchEpisodeFragment_ episodefragment;
        private List<Movie> movies;
        private List<TvShow> shows;
        private List<TvEntity> episodes;
        private String lastItem;
        
        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            if (lastItem != tabId) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if(tabId.equals("movies")) {
                	if(moviefragment == null) {
                		moviefragment = new SearchMovieFragment_();
                		if(movies != null) {
                			moviefragment.setItems(movies);
                		}
                	}
                	ft.replace(mContainerId, moviefragment);
                } else if(tabId.equals("shows")) {
                	if(showfragment == null) {
                		showfragment = new SearchShowFragment_();
                		if(shows != null) {
                			showfragment.setItems(shows);
                		}
                	}
                	ft.replace(mContainerId, showfragment);
                } if(tabId.equals("episodes")) {
                	if(episodefragment == null) {
                		episodefragment = new SearchEpisodeFragment_();
                		if(episodes != null) {
                			episodefragment.setItems(episodes);
                		}
                	}
                	ft.replace(mContainerId, episodefragment);
                }
                lastItem = tabId;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
        
        public void setMovies(List<Movie> newmovies) {
        	if(moviefragment == null) {
        		movies = newmovies;
        	} else {
        		moviefragment.setItems(newmovies);
        	}
        }
        
        public void setShows(List<TvShow> newshows) {
        	if(showfragment == null) {
        		shows = newshows;
        	} else {
        		showfragment.setItems(newshows);
        	}
        }
        
        public void setEpisodes(List<TvEntity> newepisodes) {
        	if(episodefragment == null) {
        		episodes = newepisodes;
        	} else {
        		episodefragment.setItems(newepisodes);
        	}
        }
    }
}
