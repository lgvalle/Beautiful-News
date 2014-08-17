package com.lgvalle.beaufitulnews.events;

import com.lgvalle.beaufitulnews.elpais.model.Section;

/**
 * Created by lgvalle on 22/07/14.
 *
 * Event: gallery requests more elements
 */
public class RequestingMoreItemsEvent {
	/**
	 * Empty. We just need a class to model an event
	 */

	final Section section;

	public RequestingMoreItemsEvent(Section section) {
		this.section = section;
	}

	public Section getSection() {
		return section;
	}
}
