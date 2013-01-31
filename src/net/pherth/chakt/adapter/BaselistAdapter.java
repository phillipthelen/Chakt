package net.pherth.chakt.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.R.id;
import net.pherth.chakt.R.layout;
import net.pherth.chakt.adapter.ShowSeasonsAdapter.HeaderViewHolder;
import net.pherth.chakt.adapter.ShowSeasonsAdapter.ViewHolder;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.jakewharton.trakt.TraktEntity;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShowEpisode;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BaselistAdapter extends ArrayAdapter<MediaBase>  implements StickyListHeadersAdapter {

	LayoutInflater inflater;
	
	public BaselistAdapter(Context context) {
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
			holder.subinfo = (TextView) convertView.findViewById(R.id.subinfo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MediaBase entity = this.getItem(position);
		
		holder.title.setText(entity.title);
		holder.subinfo.setText(entity.year.toString());
		
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
		TextView subinfo;
	}
}