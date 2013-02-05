package net.pherth.chakt.fragments;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowProgress;
import com.jakewharton.trakt.entities.TvShowSeason;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


@EFragment(R.layout.fragment_single_show)
public class SingleBaseFragment extends SherlockFragment {
	
	@UiThread
	void showCrouton(String message, Style style) {
		Crouton.makeText(getActivity(), message, style).show();
	}
	
	@UiThread
	void networkErrorCrouton() {
		showCrouton("There was a network error", Style.ALERT);
	}
	
	@UiThread
	void updateMenuIcon(MenuItem item, Integer id) {
		item.setIcon(id);
	}
}
