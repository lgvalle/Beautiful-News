package com.lgvalle.beaufitulphotos.elpais.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by lgvalle on 31/07/14.
 */
@Root(strict = false)
public class Item {
	@Element (required = false)
	private String title;
	@Element(required = false)
	private String description;



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
