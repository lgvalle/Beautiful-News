package com.lgvalle.beaufitulphotos.events;

import com.lgvalle.beaufitulphotos.elpais.model.Section;

/**
 * Created by lgvalle on 22/07/14.
 *
 * Event: gallery requests more elements
 */
public class GalleryRequestingMoreElementsEvent {
	/**
	 * Empty. We just need a class to model an event
	 */

	final Section section;

	public GalleryRequestingMoreElementsEvent(Section section) {
		this.section = section;
	}

	public Section getSection() {
		return section;
	}
}
