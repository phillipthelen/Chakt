
package net.pherth.chakt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.googlecode.androidannotations.annotations.EActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class StartActivity
    extends SherlockActivity
{

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        context = getApplicationContext();
        TraktWrapper tw = TraktWrapper.create(context);
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        	.build();
		ImageLoader.getInstance().init(config);
        
        SharedPreferences settings = getSharedPreferences("Chakt", 0);
        String username = settings.getString("username", "");
        
        
        if (username.equals("")) {
        	Intent loginIntent = new Intent(context, LoginActivity_.class);
            startActivityForResult(loginIntent, 0);
            finish();
        } else {
        	String password = settings.getString("password", "");
        	tw.setAuthentication(username, password);
        	Intent recentIntent = new Intent(context, MainActivity_.class);
            startActivityForResult(recentIntent, 0);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
