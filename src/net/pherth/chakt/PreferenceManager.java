package net.pherth.chakt;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface PreferenceManager {


	String username();
	String password();
	
	long lastUpdated();
	
}
