package net.pherth.chakt;

import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface PreferencesManager {


	String username();
	String password();
	
	long lastUpdated();
	
}
