package net.pherth.chakt.fragments;

import java.util.List;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import net.pherth.chakt.R;
import net.pherth.chakt.SingleMovieActivity_;
import net.pherth.chakt.SingleShowActivity_;
import net.pherth.chakt.adapter.BaselistAdapter;
import net.pherth.chakt.adapter.BaselistAdapter_;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@EFragment(R.layout.fragment_baselist)
public class SearchMovieFragment extends Fragment {

	List<Movie> movies;
	
	@ViewById
	ListView list;
	
	@Bean
	BaselistAdapter adapter;
	
	@AfterViews
	public void loadList() {
		adapter.init(getActivity(), "movie");
		
		if(movies != null) {
			adapter.addAll(movies);
			adapter.notifyDataSetChanged();
		}
		
		// Assign adapter to ListView
		list.setAdapter(adapter); 
		list.setItemsCanFocus(false);
		
		list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Movie selMovie =(Movie) (parent.getItemAtPosition(position));
            	Intent recentIntent = new Intent(getActivity().getApplicationContext(), SingleMovieActivity_.class);
            	recentIntent.putExtra("movie", selMovie);
                startActivityForResult(recentIntent, 0);
            }
        });
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@UiThread
	public void setItems(List<Movie> newmovies) {
		movies = newmovies;
		if(adapter != null) {
			adapter.addAll(movies);
			adapter.notifyDataSetChanged();
		}
	}
}
