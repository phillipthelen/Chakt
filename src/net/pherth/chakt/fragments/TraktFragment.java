package net.pherth.chakt.fragments;

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
	
}
