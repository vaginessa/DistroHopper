package be.robinj.ubuntu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.commonsware.cwac.colormixer.ColorMixer;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class PreferencesActivity extends Activity
{
	private SharedPreferences prefs;

	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_preferences);

		try
		{
			SeekBar sbLauncherIcon_opacity = (SeekBar) this.findViewById (R.id.sbLauncherIcon_opacity);
			Switch swUnityBackground_dynamic = (Switch) this.findViewById (R.id.swUnityBackground_dynamic);
			ColorMixer cmUnityBackground_colour = (ColorMixer) this.findViewById (R.id.cmUnityBackground_colour);
			SeekBar sbUnityBackground_opacity = (SeekBar) this.findViewById (R.id.sbUnityBackgrond_opacity);
			Switch swColourCalc_advanced  = (Switch) this.findViewById (R.id.swColourCalc_advanced);
			Switch swColourCalc_hsv  = (Switch) this.findViewById (R.id.swColourCalc_hsv);

			this.prefs = this.getSharedPreferences ("prefs", MODE_PRIVATE);
			sbLauncherIcon_opacity.setProgress (this.prefs.getInt ((String) sbLauncherIcon_opacity.getTag (), 204));
			swUnityBackground_dynamic.setChecked (this.prefs.getBoolean ((String) swUnityBackground_dynamic.getTag (), true));
			cmUnityBackground_colour.setColor (this.prefs.getInt ((String) cmUnityBackground_colour.getTag (), Color.WHITE));
			sbUnityBackground_opacity.setProgress (this.prefs.getInt ((String) sbUnityBackground_opacity.getTag (), 50));
			swColourCalc_advanced.setChecked (this.prefs.getBoolean ((String) swColourCalc_advanced.getTag (), true));
			swColourCalc_hsv.setChecked (this.prefs.getBoolean ((String) swColourCalc_hsv.getTag (), true));

			SeekBarChangeListener seekBarChangeListener = new SeekBarChangeListener ();
			CheckedChangeListener checkedChangeListener = new CheckedChangeListener ();

			sbLauncherIcon_opacity.setOnSeekBarChangeListener (seekBarChangeListener);
			swUnityBackground_dynamic.setOnCheckedChangeListener (checkedChangeListener);
			cmUnityBackground_colour.setOnColorChangedListener (new ColorChangeListener (cmUnityBackground_colour));
			sbUnityBackground_opacity.setOnSeekBarChangeListener (seekBarChangeListener);
			swColourCalc_advanced.setOnCheckedChangeListener (checkedChangeListener);
			swColourCalc_hsv.setOnCheckedChangeListener (checkedChangeListener);

			this.unityBackground_dynamic_changed (swUnityBackground_dynamic.isChecked ());

			Tracker tracker = ((Application) this.getApplication ()).getTracker (Application.TrackerName.APP_TRACKER);
			tracker.send (new HitBuilders.AppViewBuilder ().build ());
		}
		catch (Exception ex)
		{
			ExceptionHandler exh = new ExceptionHandler (this, ex);
			exh.show ();
		}
	}


	@Override
	public boolean onCreateOptionsMenu (Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater ().inflate (R.menu.preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId ();
		/*if (id == R.id.action_settings)
		{
			return true;
		}*/
		return super.onOptionsItemSelected (item);
	}

	public void btnBack_clicked (View view)
	{
		try
		{
			this.onBackPressed ();
		}
		catch (Exception ex)
		{
			ExceptionHandler exh = new ExceptionHandler (this, ex);
			exh.show ();
		}
	}

	public void btnAbout_clicked (View view)
	{
		try
		{
			Intent intent = new Intent (this, AboutActivity.class);
			this.startActivity (intent);
		}
		catch (Exception ex)
		{
			ExceptionHandler exh = new ExceptionHandler (this, ex);
			exh.show ();
		}
	}

	private void unityBackground_dynamic_changed (boolean enabled)
	{
		LinearLayout llUnityBackground_colour = (LinearLayout) this.findViewById (R.id.llUnityBackground_colour);
		llUnityBackground_colour.setVisibility (enabled ? View.GONE : View.VISIBLE);
	}

	private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser)
		{
			try
			{
				if (fromUser)
				{
					SharedPreferences.Editor editor = PreferencesActivity.this.prefs.edit ();
					editor.putInt ((String) seekBar.getTag (), progress);

					editor.apply ();
				}
			}
			catch (Exception ex)
			{
				ExceptionHandler exh = new ExceptionHandler (PreferencesActivity.this, ex);
				exh.show ();
			}
		}

		@Override
		public void onStartTrackingTouch (SeekBar seekBar)
		{
		}

		@Override
		public void onStopTrackingTouch (SeekBar seekBar)
		{
		}
	}

	private class CheckedChangeListener implements CompoundButton.OnCheckedChangeListener
	{
		@Override
		public void onCheckedChanged (CompoundButton buttonView, boolean isChecked)
		{
			try
			{
				SharedPreferences.Editor editor = PreferencesActivity.this.prefs.edit ();
				String property = (String) buttonView.getTag ();
				editor.putBoolean (property, isChecked);

				if ("unitybackground_dynamic".equals (property))
					PreferencesActivity.this.unityBackground_dynamic_changed (isChecked);

				editor.apply ();
			}
			catch (Exception ex)
			{
				ExceptionHandler exh = new ExceptionHandler (PreferencesActivity.this, ex);
				exh.show ();
			}
		}
	}

	private class ColorChangeListener implements ColorMixer.OnColorChangedListener
	{
		private ColorMixer colorMixer;

		public ColorChangeListener (ColorMixer colorMixer)
		{
			this.colorMixer = colorMixer;
		}

		@Override
		public void onColorChange (int argb)
		{
			SharedPreferences.Editor editor = PreferencesActivity.this.prefs.edit ();
			editor.putInt ((String) this.colorMixer.getTag (), argb);

			editor.apply ();
		}
	}
}