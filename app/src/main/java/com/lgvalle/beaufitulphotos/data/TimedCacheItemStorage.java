package com.lgvalle.beaufitulphotos.data;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgvalle on 10/08/14.
 * <p/>
 * This aims to be a Cache for API requests.
 * Persisted objects would only live for a certain amount of time
 */


public class TimedCacheItemStorage implements ItemStorage {
	private static final String TAG = TimedCacheItemStorage.class.getSimpleName();
	private final int timemillis;
	private final PrefsManager prefs;

	public TimedCacheItemStorage(int timemillis, PrefsManager prefs) {
		this.timemillis = timemillis;
		this.prefs = prefs;
	}

	@Override
	public void saveItemsMap(Map<Section, List<Item>> map) {
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(map);
		prefs.saveItemsMap(json);
		prefs.saveTime();
	}

	@Override
	public Map<Section, List<Item>> getItemsMap() {
		HashMap<Section, List<Item>> map = new HashMap<Section, List<Item>>();
		if (isValidDataAge()) {
			Log.d(TAG, "[TimedCacheItemStorage - getItemsMap] - (line 40): " + "saved data still valid");
			String json = prefs.getItemsMap();
			if (json != null) {
				Gson gson = new GsonBuilder().create();
				map = gson.fromJson(json, map.getClass());
			}
		} else {
			prefs.clearItemsMap();
			Log.d(TAG, "[TimedCacheItemStorage - getItemsMap] - (line 48): " + "too old data");
		}
		return map;
	}

	private boolean isValidDataAge() {
		long savingTime = prefs.getSavingTime();
		Log.d(TAG, "[TimedCacheItemStorage - isValidDataAge] - (line 58): " + "valid window: "+(savingTime + timemillis));
		Log.d(TAG, "[TimedCacheItemStorage - isValidDataAge] - (line 59): " + "current time: "+System.currentTimeMillis());
		return (savingTime + timemillis > System.currentTimeMillis());
	}

}
