package com.lgvalle.beaufitulphotos.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by lgvalle on 10/08/14.
 */
public class PrefsManager {
	private static final String TAG = PrefsManager.class.getSimpleName();
	private static final String DATA_ITEMS_SECTION_MAP = "data_items_section_map";
	private static final String DATA_SAVING_TIME = "data_saving_time";
	private static PrefsManager instance;
	private Context ctx = null;

	public static PrefsManager getInstance(Context context) {
		if (instance == null) {
			instance = new PrefsManager(context.getApplicationContext());
		}
		return instance;
	}

	private PrefsManager(Context ctx) {
		this.ctx = ctx;
	}

	public void saveItemsMap(String json) {
		Log.d(TAG, "[PrefsManager - saveItemsMap] - (line 29): " + "saving items map");
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		sp.edit().putString(DATA_ITEMS_SECTION_MAP, json).apply();
	}

	public String getItemsMap() {
		Log.d(TAG, "[PrefsManager - getItemsMap] - (line 36): " + "getting items map");
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		return sp.getString(DATA_ITEMS_SECTION_MAP, null);
	}

	public void saveTime() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		sp.edit().putLong(DATA_SAVING_TIME, System.currentTimeMillis()).apply();
	}

	public long getSavingTime() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		return sp.getLong(DATA_SAVING_TIME, 0);
	}

	public void clearItemsMap() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		sp.edit().remove(DATA_ITEMS_SECTION_MAP).apply();
	}
}
