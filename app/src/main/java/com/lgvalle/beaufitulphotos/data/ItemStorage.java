package com.lgvalle.beaufitulphotos.data;

import com.lgvalle.beaufitulphotos.elpais.model.Item;

import java.util.List;
import java.util.Map;

/**
 * Created by lgvalle on 10/08/14.
 */
public interface ItemStorage {
	public void saveItemsMap(Map<String, List<Item>> map);
	public Map<String, java.util.ArrayList<Item>> getItemsMap();
}
