package net.pherth.chakt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Response;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@EActivity
public class PreferencesActivity extends PreferenceActivity {
	  @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        addPreferencesFromResource(R.xml.preferences);  
	  
	    }  
}
