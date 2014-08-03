package com.lgvalle.beaufitulphotos.events;

import com.lgvalle.beaufitulphotos.elpais.model.Item;
import com.lgvalle.beaufitulphotos.elpais.model.Section;

/**
 * Created by lgvalle on 03/08/14.
 */
public class NewsItemChosen {

	final Item item;
	final Section section;

	public NewsItemChosen(Section section, Item item) {
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
