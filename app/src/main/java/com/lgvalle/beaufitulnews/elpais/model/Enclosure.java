package com.lgvalle.beaufitulnews.elpais.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by lgvalle on 02/08/14.
 */
@Root(strict = false)
public class Enclosure implements Parcelable {
	public static final String TYPE_IMAGE = "image/jpeg";
	public static final String TYPE_VIDEO = "video/m4v";

	@Attribute
	private String url;

	@Attribute
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
		dest.writeString(this.type);
		dest.writeString(this.value);
	}

	public Enclosure() {
	}

	private Enclosure(Parcel in) {
		this.url = in.readString();
		this.type = in.readString();
		this.value = in.readString();
	}

	public static final Creator<Enclosure> CREATOR = new Creator<Enclosure>() {
		public Enclosure createFromParcel(Parcel source) {
			return new Enclosure(source);
		}

		public Enclosure[] newArray(int size) {
			return new Enclosure[size];
		}
	};
}
