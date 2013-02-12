package net.pherth.chakt.adapter;

import net.pherth.chakt.R;
import net.pherth.chakt.TraktWrapper;
import net.pherth.chakt.adapter.BaselistAdapter.ViewHolder;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.taig.pmc.PopupMenuCompat;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EBean
public class ShowSeasonsAdapter extends ArrayAdapter<TvShowEpisode>  implements StickyListHeadersAdapter {

	
	private LayoutInflater inflater;
	
	@RootContext
	Context cxt;

	TraktWrapper tw;
	FragmentActivity activity;
	TvShow show;
	
	public ShowSeasonsAdapter(Context context) {
	    super(context, R.layout.fragment_baselist);
	    inflater = LayoutInflater.from(context);
	    this.cxt = context;
	    this.tw = TraktWrapper.getInstance();
	}
	
	public void init(FragmentActivity activity, TvShow show) {
		this.activity = activity;
		this.show = show;
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
		
		TvShowEpisode entity = this.getItem(position);
		holder.contextbutton.setTag(entity);
		holder.contextbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final TvShowEpisode entry = (TvShowEpisode) v.getTag();
				
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
		
		return convertView;
	}


	@Override public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.listitem_seasonlist_header, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		//set header text as first char in name
		int seasonNumber = this.getItem(position).season;
		String headerText;
		if(seasonNumber == 0){
			headerText = "Specials";
		}else if(seasonNumber == 1){
			headerText = "Season 1";
		} else {
			headerText = "Seasons " + String.valueOf(seasonNumber);
		}
		holder.text.setText(headerText);

		return convertView;
	}

	//remember that these have to be static, postion=1 should walys return the same Id that is.
	@Override
	public long getHeaderId(int position) {
		//return the first character of the country as ID because this is what headers are based upon
		return this.getItem(position).season;
	}
	
	class HeaderViewHolder {
		TextView text;
	}
	
	class ViewHolder {
		TextView title;
		TextView subinfo;
		ImageButton contextbutton;
	}
	
	@Background
	void checkin(TvShowEpisode entry) {
		try {
			tw.checkinEpisode(show, entry);
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, activity), Style.ALERT);
			return;
		}
		this.displayCrouton(R.string.episodeCheckin, Style.CONFIRM);
	}
	
	@Background
	void watchlist(TvShowEpisode entry) {
		try {
			tw.switchWatchlistEpisode(show, entry);
		} catch (TraktException e) {
			displayCrouton(tw.handleError(e, activity), Style.ALERT);
			return;
		}
		if (entry.inWatchlist) {
			this.displayCrouton(R.string.episodeRemove, Style.CONFIRM);
			entry.inWatchlist = false;
		} else {
			this.displayCrouton(R.string.episodeAdd, Style.CONFIRM);
			entry.inWatchlist = true;
		}
	}
	
	@UiThread
	void displayCrouton(Integer resourceId, Style style) {
		Crouton.showText(activity, resourceId, style);
	}
	
	@UiThread
	void displayCrouton(String message, Style style) {
		Crouton.showText(activity, message, style);
	}
}