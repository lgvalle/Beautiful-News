package com.lgvalle.beaufitulphotos.elpais.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by lgvalle on 31/07/14.
 */
@Root(strict = false)
public class Item {
	@Element (required = false)
	private String title;
	@Element(required = false)
	private String description;
	@ElementList(inline = true, required = false)
	private List<Enclosure> enclosure;


	public List<Enclosure> getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(List<Enclosure> enclosure) {
		this.enclosure = enclosure;
	}

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
