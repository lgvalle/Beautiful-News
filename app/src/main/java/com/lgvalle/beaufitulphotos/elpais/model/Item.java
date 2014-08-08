package com.lgvalle.beaufitulphotos.elpais.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgvalle on 31/07/14.
 */
@Root(strict = false)
public class Item implements Parcelable {


	@Element(required = false)
	private String title;
	@ElementList(entry="description", inline=true, required = false)
	private List<String> description;
	@Element(required = false)
	@Namespace(reference = "http://purl.org/rss/1.0/modules/content/", prefix = "content")
	private String encoded;
	@Element(required = false)
	private String pubDate;
	@Element(required = false)
	private String link;

	@ElementList(inline = true, required = false)
	private List<Enclosure> enclosures;


	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Enclosure> getEnclosures() {
		return enclosures;
	}

	public void setEnclosures(List<Enclosure> enclosures) {
		this.enclosures = enclosures;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}


	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getImageURLSmall() {
		String image = null;
		if (enclosures != null) {
			image = enclosures.get(0).getUrl();
			if (enclosures.size() > 1) {
				image = enclosures.get(1).getUrl();
			}
		}
		return image;
	}

	public String getImageURLLarge() {
		String image = null;
		for (Enclosure enclosure : enclosures) {
			if (enclosure.getType().equals(Enclosure.TYPE_IMAGE)) {
				image = enclosure.getUrl();
			}
		}
		return image;
	}


	public Item() {
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeStringList(this.description);
		dest.writeString(this.encoded);
		dest.writeString(this.pubDate);
		dest.writeString(this.link);
		dest.writeTypedList(enclosures);
	}

	private Item(Parcel in) {
		this.title = in.readString();
		description = new ArrayList();
		in.readStringList(description);
		this.encoded = in.readString();
		this.pubDate = in.readString();
		this.link = in.readString();
		enclosures = new ArrayList();
		in.readTypedList(enclosures, Enclosure.CREATOR);
	}

	public static final Creator<Item> CREATOR = new Creator<Item>() {
		public Item createFromParcel(Parcel source) {
			return new Item(source);
		}

		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
}
