package net.pherth.chakt.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.R.id;
import net.pherth.chakt.R.layout;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.jakewharton.trakt.TraktEntity;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.jakewharton.trakt.entities.TvShowSeason;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ShowSeasonsAdapter extends ArrayAdapter<TvShowEpisode>  implements StickyListHeadersAdapter {

	
	private LayoutInflater inflater;

	public ShowSeasonsAdapter(Context context) {
	    super(context, R.layout.fragment_baselist);
	    inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_seasonlist, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		TvShowEpisode episode = this.getItem(position);
		
		holder.title.setText(episode.title);

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
		if(seasonNumber%2 == 0){
			headerText = "Season " + String.valueOf(seasonNumber);
		}else{
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
	}
}