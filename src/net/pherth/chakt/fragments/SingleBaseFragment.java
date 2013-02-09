package net.pherth.chakt.fragments;

import net.pherth.chakt.R;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


@EFragment(R.layout.fragment_single_show)
public class SingleBaseFragment extends SherlockFragment {
	
	@UiThread
	void displayCrouton(String message, Style style) {
		Crouton.showText(getActivity(), message, style);
	}
	
	void displayCrouton(Integer resourceId, Style style) {
		this.displayCrouton(getString(resourceId), style);
	}
	
	@UiThread
	void networkErrorCrouton() {
		displayCrouton("There was a network error", Style.ALERT);
	}
	
	@UiThread
	void updateMenuIcon(MenuItem item, Integer id) {
		item.setIcon(id);
	}
}
