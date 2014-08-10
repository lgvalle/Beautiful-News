package com.lgvalle.beaufitulnews.events;

import com.lgvalle.beaufitulnews.elpais.model.Item;

import java.util.List;

/**
 * Created by lgvalle on 31/07/14.
 */
public class NewsAvailableEvent {

	List<Item> items;

	public NewsAvailableEvent(List<Item> item) {
		this.items = item;
	}


	public List<Item> getItems() {
		return items;
	}
}
