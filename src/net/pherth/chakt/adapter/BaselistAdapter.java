package net.pherth.chakt.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.pherth.chakt.R;
import net.pherth.chakt.R.id;
import net.pherth.chakt.R.layout;

import com.jakewharton.trakt.TraktEntity;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BaselistAdapter extends ArrayAdapter<MediaBase> {

	public BaselistAdapter(Context context) {
	    super(context, R.layout.fragment_baselist);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.listitem_baselist, null);
	
	    }

    	MediaBase entity = this.getItem(position);
	
	    if (entity != null) {
	    	Button checkinbutton = (Button) v.findViewById(R.id.checkinbutton);
	        TextView titleView = (TextView) v.findViewById(R.id.title);
	        TextView subinfoView = (TextView) v.findViewById(R.id.subinfo);
	        
	        titleView.setText(entity.title);
	        subinfoView.setText(String.valueOf(entity.year));
	    }
	
	    return v;

	}
	
}