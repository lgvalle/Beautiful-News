package com.lgvalle.beaufitulnews.utils;

import android.util.Log;
import com.crashlytics.android.Crashlytics;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeHelper {

	// Sun, 10 Aug 2014 13:54:50 +0200
	// Sun, 10 Aug 2014 17:56:41 +0200
	private static final String TAG = TimeHelper.class.getSimpleName();
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy kk:mm:ss Z";
	public static long getTimestamp(String date) {
		Timestamp timestamp = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT,  Locale.ENGLISH);
			Date parsedDate = dateFormat.parse(date);
			timestamp = new Timestamp(parsedDate.getTime());
		} catch (Exception e) {
			Log.e(TAG, "[TimeHelper - getTimestamp] - (line 20): " + "", e);
			Crashlytics.logException(e);
			timestamp = new Timestamp(System.currentTimeMillis());
		}
		return timestamp.getTime();
	}

}
