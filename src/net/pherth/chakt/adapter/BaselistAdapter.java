package net.pherth.chakt.adapter;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleEpisodeActivity_;
import net.pherth.chakt.TraktWrapper;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Activity;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.Response;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.taig.pmc.PopupMenuCompat;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EBean
public class BaselistAdapter extends ArrayAdapter<MediaBase> {

	LayoutInflater inflater;
	String type;
	
	@RootContext
	Context cxt;
	
	TraktWrapper tw;
	FragmentActivity activity;
	
	public BaselistAdapter(Context context) {
	    super(context, R.layout.fragment_baselist);
	    inflater = LayoutInflater.from(context);
	    this.cxt = context;
	    this.tw = TraktWrapper.getInstance();
	}
	
	public void init(FragmentActivity activity, String type) {
		this.activity = activity;
		this.type = type;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_baselist, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.subinfo = (TextView) convertView.findViewById(R.id.subinfo);
			holder.contextbutton = (ImageButton) convertView.findViewById(R.id.contextbutton);
			holder.contextbutton.setFocusable(false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MediaBase entity = this.getItem(position);
		holder.contextbutton.setTag(entity);
		holder.contextbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final MediaBase entry = (MediaBase) v.getTag();
				
				PopupMenuCompat menu = PopupMenuCompat.newInstance( cxt, v );
				menu.inflate( R.menu.contextmenu );
				menu.setOnMenuItemClickListener( new PopupMenuCompat.OnMenuItemClickListener()
				{

					@Override
					public boolean onMenuItemClick(android.view.MenuItem item) {
						switch (item.getItemId()) {
					        case R.id.checkincontext:
					        	checkin(entry);
					        	break;
					        case R.id.watchlistcontext:
					        	watchlist(entry);
					        	break;
						}
						
						
						return false;
					}
				} );

				menu.show();
			}
			
		});
		
		
		holder.title.setText(entity.title);
		holder.subinfo.setText(entity.year.toString());
		
		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView subinfo;
		ImageButton contextbutton;
	}
	
	@Background
	void checkin(MediaBase entry) {
		this.displayCrouton(R.string.tryCheckin, Style.INFO);
		if(type=="movie") {
			try {
				tw.checkinMovie((Movie) entry);
			} catch (TraktException e) {
				tw.handleError(e, activity);
				return;
			}
			this.displayCrouton(R.string.movieCheckin, Style.CONFIRM);
		} else if(type=="show") {
			try {
				tw.checkinShow((TvShow) entry);
			} catch (TraktException e) {
				tw.handleError(e, activity);
				return;
			}
			this.displayCrouton(R.string.showCheckin, Style.CONFIRM);
		} else if(type=="episode") {
		}
	}
	
	@Background
	void watchlist(MediaBase entry) {
		if(entry.inWatchlist == null) {
			entry.inWatchlist = false;
		}
		if (entry.inWatchlist) {
			displayCrouton(R.string.tryWatchlistRemove, Style.INFO);
		} else {
			displayCrouton(R.string.tryWatchlistAdd, Style.INFO);
		}
		if(type=="movie") {
			try {
				tw.switchWatchlistMovie((Movie) entry);
			} catch (TraktException e) {
				tw.handleError(e, activity);
				return;
			}
			if (entry.inWatchlist) {
				this.displayCrouton(R.string.movieRemove, Style.CONFIRM);
				entry.inWatchlist = false;
			} else {
				this.displayCrouton(R.string.movieAdd, Style.CONFIRM);
				entry.inWatchlist = true;
			}
		} else if(type=="show") {
			try {
				tw.switchWatchlistShow((TvShow) entry);
			} catch (TraktException e) {
				tw.handleError(e, activity);
				return;
			}
			if (entry.inWatchlist) {
				this.displayCrouton(R.string.showRemove, Style.CONFIRM);
				entry.inWatchlist = false;
			} else {
				this.displayCrouton(R.string.showAdd, Style.CONFIRM);
				entry.inWatchlist = true;
			}
		} else if(type=="episode") {
		}
	}
	
	@UiThread
	void displayCrouton(Integer resourceId, Style style) {
		Crouton.showText(activity, resourceId, style);
	}
}