package com.lgvalle.beaufitulnews;

import android.app.Application;
import com.lgvalle.beaufitulnews.utils.TypefaceUtil;

/**
 * Created by lgvalle on 21/07/14.
 */
public class BeautifulNewsApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// Replace font typeface in all application
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/NoticiaText-Regular.ttf");
	}

}
