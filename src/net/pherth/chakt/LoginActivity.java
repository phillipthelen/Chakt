package net.pherth.chakt;

import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import roboguice.inject.InjectView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.trakt.TraktApiService;
import com.jakewharton.trakt.TraktEntity;
import com.jakewharton.trakt.TraktException;
import com.jakewharton.trakt.entities.Response;
import com.jakewharton.trakt.entities.TvEntity;
/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {
	
	@Inject ProgressDialog mProgressDialog;

	TraktWrapper tw;
	
	@Inject Context context;
	
	// Values for Username and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;

	// UI references.
	@ViewById(R.id.username)
	EditText mUsernameView;
	@ViewById(R.id.password)
	EditText mPasswordView;
	@ViewById(R.id.login_form)
	View mLoginFormView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        tw = TraktWrapper.getInstance();
	}
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid Username, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	@Click
	public void signinbuttonClicked() {
		
		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid Username address.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			mUsername = mUsernameView.getText().toString();
			mPassword = mPasswordView.getText().toString();
			login();
			mProgressDialog = ProgressDialog.show(LoginActivity.this,
		              getString( R.string.pleaseWait), getString(R.string.loggingin), true);
		}
	}
	
	@Background
	public void login() {
		tw.setAuthentication(mUsername, mPassword);
    	Response r = new Response();
    	try {
    		r = tw.accountService().test().fire();
    	} catch (TraktException e) {
    		Log.e("LoginActivity", e.toString());
    		loginFailed();
    		return;
    	}
    	String status = r.status;
    	Log.i("LoginActivity", status);
    	if (status.equals("success")) {
    		SharedPreferences sharedPref = this.getSharedPreferences("Chakt", 0);
    		SharedPreferences.Editor editor = sharedPref.edit();
    	      editor.putString("username", mUsername);
    	      editor.putString("password", mPassword);
    	      editor.commit();
    	      loginSuccess();
    	} else {
    		loginFailed();
    	}
		
    	
    }
	
	@UiThread
	public void loginSuccess() {
        mProgressDialog.dismiss();
        Intent recentIntent = new Intent(context, MainActivity_.class);
        startActivityForResult(recentIntent, 0);
        //Crouton.makeText(recentIntent, "Login successfull!", Style.CONFIRM).show();
        finish();
        
    }
	
	@UiThread
	public void loginFailed() {
		mProgressDialog.dismiss();
    	mUsernameView.setText("");
    	mPasswordView.setText("");
		mUsernameView.requestFocus();
		Crouton.showText(this,  "Login failed.", Style.ALERT);
	}
}
