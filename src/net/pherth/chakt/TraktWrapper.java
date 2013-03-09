package net.pherth.chakt;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.UiThread;
import com.jakewharton.apibuilder.ApiException;
import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.TraktEntity;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.Response;
import com.jakewharton.trakt.entities.TvEntity;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowEpisode;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EBean
public class TraktWrapper extends ServiceManager{
	
	@Bean
	static TraktWrapper traktWrapper;
	
	private Context context;
	public String username;
	
	public MediaBase currentItem;
	public Class<?> currentItemActivity;
	
	public static synchronized TraktWrapper getInstance() {	
		if (traktWrapper == null) {
			
			return null;
		}
		return traktWrapper;
	}

	public TraktWrapper(Context context) {	
		this.context = context;
		this.setApiKey(this.context.getString(R.string.traktKey));
	}
	
	public static TraktWrapper create(Context context) {
		traktWrapper = new TraktWrapper(context);
		return traktWrapper;
	}
	
	@Override
	public ServiceManager setAuthentication(String username, String password) {
		ServiceManager sm = super.setAuthentication(username, password);
		this.username = username;
		return sm;
	}
	
	@Background
	public void switchWatchlistMovie(Movie movie) {
		if (movie.inWatchlist) {
			traktWrapper.movieService().unwatchlist().movie(movie.imdbId).fire();
		} else {
			traktWrapper.movieService().watchlist().movie(movie.imdbId).fire();
		}
	}

	@Background
	public void switchWatchlistShow(TvShow show) {
		if (show.inWatchlist != null || show.inWatchlist) {
			traktWrapper.showService().unwatchlist().imdbId(show.imdbId).fire();
		} else {
			traktWrapper.showService().watchlist().imdbId(show.imdbId).fire();
		}
	}

	@Background
	public void switchWatchlistEpisode(TvShow show, TvShowEpisode episode) {
		if (episode.inWatchlist) {
			traktWrapper.showService().episodeUnwatchlist(show.imdbId).episode(episode.season, episode.number).fire();
		} else {
			traktWrapper.showService().episodeWatchlist(show.imdbId).episode(episode.season, episode.number).fire();
		}
	}

	@Background
	public void checkinShow(TvShow show) {
		//TODO: get newest unwatched episode and checkin
	}

	@Background
	public void checkinMovie(Movie movie) {
		Response r = traktWrapper.movieService().checkin(movie.imdbId).fire();
		if(r.status.equals("failure")) {
			throw new TraktException("None", null, new ApiException("checkinfailed"), r);
		}
		traktWrapper.currentItem = (MediaBase) movie;
		traktWrapper.currentItemActivity = SingleMovieActivity.class;
	}

	@Background
	public void checkinEpisode(TvShow show, TvShowEpisode episode) {
		Response r = traktWrapper.showService()
				.checkin(Integer.parseInt(show.tvdbId))
				.season(episode.season)
				.episode(episode.number)
				.fire();
		if(r.status.equals("failure")) {
			throw new TraktException("None", null, new ApiException("checkinfailed"), r);
		}
		//traktWrapper.currentItem = (MediaBase) episode;
		traktWrapper.currentItemActivity = SingleEpisodeActivity.class;
	}
	
	public void checkinEpisode(TvEntity entity) {
		checkinEpisode(entity.show, entity.episode);
	}
	
	public void switchWatchlistEpisode(TvEntity entity) {
		switchWatchlistEpisode(entity.show, entity.episode);
	}
	
	public String handleError(TraktException e, FragmentActivity activity) {
		System.out.println(e.getMessage());
		System.out.println(e.getResponse());
		System.out.println(e.getUrl());
		if(e.getCause().getMessage().equals("checkinfailed")) {
			return e.getMessage();
		}
		else return context.getString(R.string.unhandledError);
	}
	
	
	@UiThread
	public void displayCrouton(FragmentActivity activity, Integer resourceId, Style style) {
		Crouton.showText(activity, resourceId, style);
	}
}
