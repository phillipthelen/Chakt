package net.pherth.chakt.adapter;

import net.pherth.chakt.R;
import net.pherth.chakt.TraktWrapper;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.TvEntity;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.taig.pmc.PopupMenuCompat;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EBean
public class EpisodelistAdapter extends ArrayAdapter<TvEntity>  implements StickyListHeadersAdapter {

	LayoutInflater inflater;
	String type;
	
	@RootContext
	Context cxt;
	
	TraktWrapper tw;
	FragmentActivity activity;
	
	public EpisodelistAdapter(Context context) {
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
		
		TvEntity entity = this.getItem(position);
		holder.contextbutton.setTag(entity);
		holder.contextbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final TvEntity entry = (TvEntity) v.getTag();
				System.out.println(entry.toString());
				
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
		
		
		holder.title.setText(entity.episode.title);
		holder.subinfo.setText(entity.show.title);
		
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.listitem_seasonlist_header, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		char headerChar = getHeaderChar(this.getItem(position).episode.title);
		holder.text.setText(String.valueOf(headerChar));

		return convertView;
	}

	//remember that these have to be static, postion=1 should walys return the same Id that is.
	@Override
	public long getHeaderId(int position) {
		return getHeaderChar(this.getItem(position).episode.title);
	}
	
	private char getHeaderChar(String title) {
		char headerChar;
		//set header text as first char in name
		
		if(title.length() > 3) { 
			if(title.subSequence(0, 3).toString().equalsIgnoreCase("the")) {
				headerChar = title.subSequence(4, 5).charAt(0);
			} else {
				headerChar = title.subSequence(0, 1).charAt(0);
			}
		} else {
			headerChar = title.subSequence(0, 1).charAt(0);
		}
		return headerChar;
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
	void checkin(TvEntity entry) {
		this.displayCrouton(R.string.tryCheckin, Style.INFO);
		try {
			tw.checkinEpisode(entry);
		} catch (TraktException e) {
			tw.handleError(e, activity);
			return;
		}
		this.displayCrouton(R.string.episodeCheckin, Style.CONFIRM);
	}
	
	@Background
	void watchlist(TvEntity entry) {
		if (entry.episode.inWatchlist) {
			displayCrouton(R.string.tryWatchlistRemove, Style.INFO);
		} else {
			displayCrouton(R.string.tryWatchlistAdd, Style.INFO);
		}
		try {
			tw.switchWatchlistEpisode(entry);
		} catch (TraktException e) {
			tw.handleError(e, activity);
			return;
		}
		if (entry.episode.inWatchlist) {
			this.displayCrouton(R.string.episodeRemove, Style.CONFIRM);
			entry.episode.inWatchlist = false;
		} else {
			this.displayCrouton(R.string.episodeAdd, Style.CONFIRM);
			entry.episode.inWatchlist = true;
		}
	}
	
	@UiThread
	void displayCrouton(Integer resourceId, Style style) {
		Crouton.showText(activity, resourceId, style);
	}

	public void add(TvShow show, TvShowEpisode episode) {
		TvEntity entity = new TvEntity();
		entity.show = show;
		entity.episode = episode;
		this.add(entity);
	}
}