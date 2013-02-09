package net.pherth.chakt.adapter;
import net.pherth.chakt.fragments.EpisodeWatchlistFragment_;
import net.pherth.chakt.fragments.MovieWatchlistFragment_;
import net.pherth.chakt.fragments.ShowWatchlistFragment_;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class WatchlistsFragmentAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[] { "Movies", "Shows", "Episodes" };

    private int mCount = CONTENT.length;

    public WatchlistsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	switch(position) {
	    	case 0:
	    		return MovieWatchlistFragment_.newInstance();
	    	case 1:
	    		return ShowWatchlistFragment_.newInstance();
	    	case 2:
	    		return EpisodeWatchlistFragment_.newInstance();
    	}
    	return MovieWatchlistFragment_.newInstance();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return WatchlistsFragmentAdapter.CONTENT[position];
    }
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}