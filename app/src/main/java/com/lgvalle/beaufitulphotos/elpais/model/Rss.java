package com.lgvalle.beaufitulphotos.elpais.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by lgvalle on 31/07/14.
 */
@Root(strict = false)
public class Rss {

	@Element
	private Channel channel;

	@Element(required = false)
	private String version;


	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
