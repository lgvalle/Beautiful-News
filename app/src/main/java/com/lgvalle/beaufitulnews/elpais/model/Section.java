package com.lgvalle.beaufitulnews.elpais.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lgvalle on 02/08/14.
 */
public class Section implements Parcelable{
	private final String param;
	private final String title;
	private final int color;

	public Section(String param, String title, int color) {
		this.param = param;
		this.title = title;
		this.color = color;
	}

	public String getParam() {
		return param;
	}

	public String getTitle() {
		return title;
	}

	public int getColor() {
		return color;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.param);
		dest.writeString(this.title);
		dest.writeInt(this.color);
	}

	private Section(Parcel in) {
		this.param = in.readString();
		this.title = in.readString();
		this.color = in.readInt();
	}

	public static final Creator<Section> CREATOR = new Creator<Section>() {
		public Section createFromParcel(Parcel source) {
			return new Section(source);
		}

		public Section[] newArray(int size) {
			return new Section[size];
		}
	};
}