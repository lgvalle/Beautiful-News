package com.lgvalle.beaufitulnews.data;

import com.lgvalle.beaufitulnews.elpais.model.Item;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lgvalle on 10/08/14.
 */
public interface ItemStorage {
	public void saveItemsMap(Map<String, ArrayList<Item>> map);
	public Map<String, ArrayList<Item>> getItemsMap();
}
