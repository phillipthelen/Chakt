package net.pherth.chakt;

import net.pherth.chakt.fragments.SingleEpisodeFragment_;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.jakewharton.trakt.entities.TvShowEpisode;

@EActivity(R.layout.activity_single_episode)
@OptionsMenu(R.menu.activity_single)
public class SingleEpisodeActivity extends SherlockFragmentActivity {

	@FragmentById
	SingleEpisodeFragment_ single_episode_fragment;
	
	TvShowEpisode episode;

}
