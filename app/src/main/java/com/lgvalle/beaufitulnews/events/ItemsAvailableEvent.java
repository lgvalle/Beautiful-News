package com.lgvalle.beaufitulnews.events;

import com.lgvalle.beaufitulnews.elpais.model.Section;

import java.util.List;

/**
 * Created by lgvalle on 02/08/14.
 */
public class ItemsAvailableEvent<E, T extends Section> {

	private final T section;
	private final List<E> items;

	public ItemsAvailableEvent(List<E> item, T section) {
		this.items = item;
		this.section = section;
	}

	public T getSection() {
		return section;
	}

	public List<E> getItems() {
		return items;
	}
}
