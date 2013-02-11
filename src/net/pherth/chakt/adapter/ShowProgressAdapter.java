package net.pherth.chakt.adapter;

import net.pherth.chakt.R;
import net.pherth.chakt.TraktWrapper;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.UiThread;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;
import com.taig.pmc.PopupMenuCompat;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EBean
public class ShowProgressAdapter extends ArrayAdapter<TvShow>  implements StickyListHeadersAdapter {

	
	private LayoutInflater inflater;
	private Context cxt;
	
	TraktWrapper tw;
	FragmentActivity activity;

	public ShowProgressAdapter(Context context) {
	    super(context, R.layout.fragment_baselist);
	    inflater = LayoutInflater.from(context);
	    this.cxt = context;
	    tw = TraktWrapper.getInstance();
	}
	
	public void init(FragmentActivity activity) {
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_progresslist, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
			holder.progressLabel = (TextView) convertView.findViewById(R.id.progressLabel);
			holder.contextbutton = (ImageButton) convertView.findViewById(R.id.contextbutton);
			holder.contextbutton.setFocusable(false);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		TvShow show = this.getItem(position);
		
		holder.contextbutton.setTag(show);
		holder.contextbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final TvShow entry = (TvShow) v.getTag();
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
		
		holder.title.setText(show.title);
		holder.progress.setProgress(show.progress.percentage);
		holder.progressLabel.setText(show.progress.completed + "/" + show.progress.aired);

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
		char headerChar = getHeaderChar(this.getItem(position).title);
		holder.text.setText(String.valueOf(headerChar));

		return convertView;
	}

	//remember that these have to be static, postion=1 should walys return the same Id that is.
	@Override
	public long getHeaderId(int position) {
		return getHeaderChar(this.getItem(position).title);
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
		ProgressBar progress;
		TextView progressLabel;
		ImageButton contextbutton;
	}
	
	@Background
	void checkin(TvShow entry) {
		try {
			tw.checkinShow((TvShow) entry);
		} catch (TraktException e) {
			tw.handleError(e, activity);
			return;
		}
		this.displayCrouton(R.string.showCheckin, Style.CONFIRM);
	}
	
	@Background
	void watchlist(TvShow entry) {
		System.out.println(entry.inWatchlist);
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
	}
	
	@UiThread
	void displayCrouton(Integer resourceId, Style style) {
		Crouton.showText(activity, resourceId, style);
	}

}
