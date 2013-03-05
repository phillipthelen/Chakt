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
public class EpisodeStickylistAdapter extends EpisodelistAdapter implements StickyListHeadersAdapter {;
	
	public EpisodeStickylistAdapter(Context context) {
	    super(context);
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
}