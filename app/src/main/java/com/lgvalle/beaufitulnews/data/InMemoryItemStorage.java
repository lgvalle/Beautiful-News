package com.lgvalle.beaufitulnews.data;

import com.lgvalle.beaufitulnews.elpais.model.Item;

import java.util.List;
import java.util.Map;

/**
 * Created by lgvalle on 10/08/14.
 */
public class InMemoryItemStorage implements ItemStorage {

	@Override
	public void saveItemsMap(Map<String, List<Item>> map) {

	}

	@Override
	public Map<String, List<Item>> getItemsMap() {
		return null;
	}
}
