package net.pherth.chakt.fragments;

import java.text.SimpleDateFormat;
import java.util.Collection;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleEpisodeActivity_;
import net.pherth.chakt.SingleShowActivity_;
import net.pherth.chakt.R.layout;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.BaselistAdapter;
import net.pherth.chakt.adapter.ShowSeasonsAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


@EFragment(R.layout.fragment_single_show)
public class SingleShowFragment extends SherlockFragment {
	
	TvShow show;
	TraktWrapper tw;
	Context cxt;
	ShowSeasonsAdapter adapter;
	
	@ViewById
	TextView titletext;
	@ViewById
	TextView airsvalue;
	@ViewById
	TextView runtimevalue;
	@ViewById
	TextView releasevalue;
	@ViewById
	TextView playsvalue;
	@ViewById
	TextView descriptiontext;
	@ViewById
	ImageView headerimage;		
	@ViewById
	TextView ratingsvalue;
	@ViewById
	StickyListHeadersListView seasonlist;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_single_show, container, false);
		show= (TvShow) getActivity().getIntent().getExtras().get("show");
		
		cxt = getActivity().getApplicationContext();
		
		LinearLayout mTop = (LinearLayout) inflater.inflate(R.layout.fragment_single_show_details, null);
		ListView seasonlist = (StickyListHeadersListView) view.findViewById(R.id.seasonlist);
		seasonlist.addHeaderView(mTop);
		adapter = new ShowSeasonsAdapter(getActivity().getApplicationContext());
		// Assign adapter to ListView
		seasonlist.setAdapter(adapter); 
		seasonlist.setItemsCanFocus(false);
		
		seasonlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	TvShowEpisode selEpisode =(TvShowEpisode) (parent.getItemAtPosition(position));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleEpisodeActivity_.class);
            	recentIntent.putExtra("episode", selEpisode);
                startActivityForResult(recentIntent, 0);
            }
        });
		
		tw = TraktWrapper.getInstance();
		
        return view;
    }
	
	@AfterViews
	void loadData() {
		Log.d("TEST1", String.valueOf(seasonlist.getHeaderViewsCount()));
		Log.d("TEST", releasevalue.toString());
		titletext.setText(show.title);
		
		if(show.airDay != null) {
		airsvalue.setText(show.airDay.toString() + " " + show.airTime);
		} else {
			airsvalue.setText(getString(R.string.noair));
		}
		releasevalue.setText(android.text.format.DateFormat.format("dd.MM.yyyy", show.firstAired));
		runtimevalue.setText(show.runtime.toString() + " " + getString(R.string.minutes));
		ratingsvalue.setText(show.ratings.percentage + "% (" + show.ratings.votes + " " + getString(R.string.votes) + ")");
		descriptiontext.setText(show.overview);
		
		
		
		ImageLoader loader = ImageLoader.getInstance();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.placeholder)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		loader.displayImage(show.images.fanart, headerimage, options);
		loadDetails();
	}
	
	@Background
	void loadDetails() {
		show = tw.showService().summary(show.tvdbId).extended().fire();
		displayDetails();
	}
		
	@UiThread
	void displayDetails() {
		Log.d(show.title, show.seasons.toString());
		for(TvShowSeason season : show.seasons) {
			adapter.addAll(season.episodes.episodes);
		}
		adapter.notifyDataSetChanged();
		Log.d(show.title, String.valueOf(adapter.getCount()));
	}
	
}
