package com.lgvalle.beaufitulphotos.elpais.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by lgvalle on 31/07/14.
 */
@Root(strict = false)
public class Portada
{
	@Element
	private Rss rss;

	public Rss getRss ()
	{
		return rss;
	}

	public void setRss (Rss rss)
	{
		this.rss = rss;
	}
}
