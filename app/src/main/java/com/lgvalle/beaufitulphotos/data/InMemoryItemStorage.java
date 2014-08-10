package com.lgvalle.beaufitulphotos.data;

import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;

import java.util.List;
import java.util.Map;

/**
 * Created by lgvalle on 10/08/14.
 */
public class InMemoryItemStorage implements ItemStorage {
	@Override
	public void saveItemsMap(Map<Section, List<Item>> map) {

	}

	@Override
	public Map<Section, List<Item>> getItemsMap() {
		return null;
	}
}
