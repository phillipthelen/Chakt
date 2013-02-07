package net.pherth.chakt;

import android.content.Context;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.Response;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class TraktWrapper extends ServiceManager{
	
	private static TraktWrapper traktWrapper;
	private Context context;
	public String username;
	
	public static synchronized TraktWrapper getInstance() {	
		if (traktWrapper == null) {
			Log.d("TEst", "NULL");
			return null;
		}
		return traktWrapper;
	}

	private TraktWrapper(Context context) {	
		this.context = context;
		this.setApiKey(context.getString(R.string.traktKey));
	}
	
	public static TraktWrapper create(Context context) {
		traktWrapper = new TraktWrapper(context);
		Log.d("test", traktWrapper.toString());
		return traktWrapper;
	}
	
	@Override
	public ServiceManager setAuthentication(String username, String password) {
		ServiceManager sm = super.setAuthentication(username, password);
		this.username = username;
		return sm;
	}
	
	public Boolean switchWatchlistMovie(Movie movie) {
		try {
			if (movie.inWatchlist) {
				traktWrapper.movieService().unwatchlist().movie(movie.imdbId).fire();
			} else {
				traktWrapper.movieService().watchlist().movie(movie.imdbId).fire();
			}
    	} catch (TraktException e) {
    		Log.e("ERROR", e.getResponse().error);
    	}
		return true;
	}
	
	public Boolean switchWatchlistShow(TvShow show) {
		try {
			if (show.inWatchlist) {
				traktWrapper.showService().unwatchlist().imdbId(show.imdbId).fire();
			} else {
				traktWrapper.showService().watchlist().imdbId(show.imdbId).fire();
			}
    	} catch (TraktException e) {
    		Log.e("ERROR", e.getResponse().error);
    	}
		return true;
	}
	
	public Boolean switchWatchlistEpisode(TvShow show, TvShowEpisode episode) {
		try {
			if (episode.inWatchlist) {
				traktWrapper.showService().episodeUnwatchlist(show.imdbId).episode(episode.season, episode.number).fire();
			} else {
				traktWrapper.showService().episodeWatchlist(show.imdbId).episode(episode.season, episode.number).fire();
			}
    	} catch (TraktException e) {
    		Log.e("ERROR", e.getResponse().error);
    	}
		return true;
	}
	
	public Response checkinShow(TvShow show) {
		//TODO: get newest unwatched episode and checkin
		return new Response();
	}
	
	public Response checkinMovie(Movie movie) {
		Response r = this.traktWrapper.movieService().checkin(movie.imdbId).fire();
		return r;
	}
	
	public Response checkinEpisode(TvShow show, TvShowEpisode episode) {
		Response r = this.traktWrapper.showService()
				.checkin(Integer.parseInt(show.tvdbId))
				.season(episode.season)
				.episode(episode.number)
				.fire();
		return r;
	}
}
