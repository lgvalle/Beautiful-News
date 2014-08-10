package com.lgvalle.beaufitulnews.events;

import com.lgvalle.beaufitulnews.interfaces.PhotoModel;

/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Event: Item selected in gallery
 */
public class GalleryItemChosenEvent {
	private PhotoModel photo;

	public GalleryItemChosenEvent(PhotoModel photo) {
		this.photo = photo;
	}

	public PhotoModel getPhoto() {
		return photo;
	}
}
