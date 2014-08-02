package com.lgvalle.beaufitulphotos.elpais.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by lgvalle on 02/08/14.
 */
@Root(strict = false)
public class Enclosure {

	@Attribute
	private String url;

	@Text(required = false)
	private String value;

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
