package com.lgvalle.beaufitulphotos.elpais.model;

import java.io.Serializable;

/**
 * Created by lgvalle on 02/08/14.
 */
public class Section implements Serializable{
	/*
	Portada("elpais", R.string.section_portada),
	Politica("politica", R.string.section_politica),
	Internacional("internacional", R.string.section_internacional),
	Sociedad("sociedad", R.string.section_sociedad),
	Deportes("deportes", R.string.section_deportes);

*/
	private final String param;
	private final int title;

	public Section(String param, int title) {
		this.param = param;
		this.title = title;
	}

	public String getParam() {
		return param;
	}

	public int getTitle() {
		return title;
	}
}