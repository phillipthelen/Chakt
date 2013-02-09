
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
import android.content.Context;
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
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;

@EActivity(R.layout.activity_main)
public class MainActivity
    extends SherlockFragmentActivity
    implements OnNavigationListener
{
	
	private final List<ItemInfo> mItems = new ArrayList<ItemInfo>();
	
	@NonConfigurationInstance
	ItemInfo currItem;
	
	Boolean newCreated = true;
	
	class ItemInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private SherlockFragment fragment;
        private final Integer position;

        ItemInfo(String _tag, Class<?> _class, Bundle _args, Integer _position) {
            tag = _tag;
            clss = _class;
            args = _args;
            position = _position;
        }
    }
	
	@Inject Context context;
	
	@AfterViews
    void afterViews() {
        configureActionBar();
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void configureActionBar() {
        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.locations, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
        mItems.add(new ItemInfo("Show Progress", ShowProgressFragment.class, null, 0));
        mItems.add(new ItemInfo("Movie Watchlists", WatchlistsFragment.class, null, 1));
        mItems.add(new ItemInfo("Show Watchlists", WatchlistsFragment.class, null, 2));
        mItems.add(new ItemInfo("Episode Watchlists", WatchlistsFragment.class, null, 3));
        mItems.add(new ItemInfo("Lists", MovieWatchlistFragment.class, null, 4));
        if (currItem != null){
        	getSupportActionBar().setSelectedNavigationItem(currItem.position);
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    	Log.d("MainActivity", "Listitem selected");
    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	if (! newCreated || currItem == null) {
    		newCreated = false;
        	currItem = mItems.get(itemPosition);switch(itemPosition) {
        	case 0:
        		if(currItem.fragment == null) {
        			currItem.fragment = ShowProgressFragment_.newInstance();
        		}
        		break;
        	case 1:
        		if(currItem.fragment == null) {
        			currItem.fragment = MovieWatchlistFragment_.newInstance();
        		}
        		break;
        		
        	case 2:
        		if(currItem.fragment == null) {
        			currItem.fragment = ShowWatchlistFragment_.newInstance();
        		}
        		break;
        	
        	case 3:
        		if(currItem.fragment == null) {
        			currItem.fragment = EpisodeWatchlistFragment_.newInstance();
        		}
        		break;
        	
        	
        	case 4:
        		if(currItem.fragment == null) {
        			currItem.fragment = MovieWatchlistFragment_.newInstance();
        		}
        		break;
        }
    	}
        ft.replace(R.id.maincontent, currItem.fragment).commitAllowingStateLoss();
    	return true;
    }
    
    @Override  
    public void onConfigurationChanged(Configuration newConfig) {  
      Log.d("MainActivity", "SCREEN ROTATED!");
      if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {  

      } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {  

      }  
    }  

}
