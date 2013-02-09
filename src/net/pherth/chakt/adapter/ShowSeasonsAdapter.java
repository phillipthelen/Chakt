package net.pherth.chakt.adapter;

import net.pherth.chakt.R;
import net.pherth.chakt.adapter.BaselistAdapter.ViewHolder;
import android.content.Context;
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
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.TvShowEpisode;
import com.taig.pmc.PopupMenuCompat;

@EBean
public class ShowSeasonsAdapter extends BaselistAdapter {

	
	private LayoutInflater inflater;

	public ShowSeasonsAdapter(Context context) {
	    super(context);
	    inflater = LayoutInflater.from(context);
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
		
		
		holder.title.setText(entity.title);
		holder.subinfo.setText(entity.year.toString());
		
		return convertView;
	}

}