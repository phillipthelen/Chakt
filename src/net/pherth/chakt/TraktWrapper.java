package net.pherth.chakt;

import android.content.Context;
import android.util.Log;

import com.jakewharton.trakt.ServiceManager;

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
}
