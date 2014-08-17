package com.lgvalle.beaufitulnews.events;

import com.lgvalle.beaufitulnews.elpais.model.Item;
import com.lgvalle.beaufitulnews.elpais.model.Section;

/**
 * Created by lgvalle on 03/08/14.
 */
public class ItemChosenEvent {
	final Item item;
	final Section section;

	public ItemChosenEvent(Section section, Item item) {
		this.section = section;
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	public Section getSection() {
		return section;
	}
}
