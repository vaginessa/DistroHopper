package be.robinj.ubuntu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.webkit.JavascriptInterface;

import java.io.File;

/**
 * Created by robin on 28/10/13.
 */
public class AppIcon extends ImageExt
{
	private AppLauncher app;

	public AppIcon (String path)
	{
		super ();

		Drawable drawable = new BitmapDrawable (MainActivity.getContext ().getResources (), path);
		this.setDrawable (drawable);
	}

	public AppIcon (Drawable drawable)
	{
		super ();

		this.setDrawable (drawable);
	}

	public AppIcon (ResolveInfo resInf, AppLauncher app)
	{
		super (resInf.loadIcon (MainActivity.getContext ().getPackageManager ()));

		this.app = app;
	}

	public AppIcon (AppLauncher app)
	{
		super (app.getIcon ().get ());

		this.app = app;
	}

	public void setApp (AppLauncher appLauncher)
	{
		this.app = appLauncher;
	}

	@Override
	@JavascriptInterface
	public String getPath ()
	{
		SharedPreferences prefs = MainActivity.getPrefs ();
		String name = "app" + Integer.toString (this.app.getId ());
		String path = prefs.getString ("icon:" + name, null);
		boolean changed = false;

		if (path == null)
		{
			path = this.getPath (name);
			changed = true;
		}
		else
		{
			File file = new File (path);

			if (! file.exists () || file.length () == 0) // Sometimes the file might exist, but it's 0 bytes //
				path = this.getPath (name);
		}

		if (changed)
		{
			SharedPreferences.Editor editor = prefs.edit ();
			editor.putString ("icon:" + name, path);

			editor.apply ();
		}

		return path;
	}
}