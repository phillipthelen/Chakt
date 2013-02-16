package net.pherth.chakt.fragments;

import net.pherth.chakt.R;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EFragment
public class TraktFragment extends SherlockFragment {

	@UiThread
	void setIndeterminateProgress(Boolean bool) {
		getActivity().setProgressBarIndeterminateVisibility(bool);
		getActivity().setProgressBarIndeterminate(bool);
	}
	
	@UiThread
	void displayCrouton(String message, Style style) {
		Crouton.showText(getActivity(), message, style);
	}
	
	void displayCrouton(Integer resourceId, Style style) {
		this.displayCrouton(getString(resourceId), style);
	}
	
	
	@UiThread
	void spawnWrongAuthDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.authfailed);
		builder.setPositiveButton(R.string.correctPassword, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	           }
	       });
	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
		AlertDialog dialog = builder.create();
	}
}
