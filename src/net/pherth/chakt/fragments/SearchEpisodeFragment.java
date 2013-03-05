package net.pherth.chakt.fragments;

import java.util.List;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvEntity;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleEpisodeActivity_;
import net.pherth.chakt.SingleShowActivity_;
import net.pherth.chakt.adapter.BaselistAdapter;
import net.pherth.chakt.adapter.BaselistAdapter_;
import net.pherth.chakt.adapter.EpisodelistAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@EFragment(R.layout.fragment_baselist)
public class SearchEpisodeFragment extends Fragment {

	List<TvEntity> episodes;
	
	@ViewById
	ListView list;
	
	@Bean
	EpisodelistAdapter adapter;
	
	@AfterViews
	public void loadList() {
		adapter.init(getActivity(), "episode");
		
		if(episodes != null) {
			adapter.addAll(episodes);
			adapter.notifyDataSetChanged();
		}
		
		// Assign adapter to ListView
		list.setAdapter(adapter); 
		list.setItemsCanFocus(false);
		list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	TvEntity entity =(TvEntity) (parent.getItemAtPosition(position));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleEpisodeActivity_.class);
            	recentIntent.putExtra("episode", entity.episode);
            	recentIntent.putExtra("show", entity.show);
                startActivityForResult(recentIntent, 0);
            }
        });
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@UiThread
	public void setItems(List<TvEntity> newepisodes) {
		episodes = newepisodes;
		if(adapter != null) {
			adapter.addAll(episodes);
			adapter.notifyDataSetChanged();
		}
	}
}
