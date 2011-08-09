package org.kth.cos.android.sw.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Parcelable {
	private int id;
	private String value;
	private String profileName;
	private String postedBy;
	private Date postedAt;

	public Status() {
	}

	public Status(int id, String value, String profileName, String postedAtStr, String postedBy) throws ParseException {
		this.id = id;
		this.value = value;
		this.profileName = profileName;
		this.postedAt = parseDate(postedAtStr);
		this.postedBy = postedBy;
	}

	public Status(Parcel in) {
		readFromParcel(in);
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(value);
		dest.writeString(profileName);
		dest.writeSerializable(postedAt);
		dest.writeString(postedBy);
	}

	private void readFromParcel(Parcel in) {
		id = in.readInt();
		value = in.readString();
		profileName = in.readString();
		postedAt = (Date) in.readSerializable();
		postedBy = in.readString();
	}

	public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
		public Status createFromParcel(Parcel in) {
			return new Status(in);
		}

		public Status[] newArray(int size) {
			return new Status[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public String getPostedAtStr() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postedAt);
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public Date parseDate(String dateStr) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateStr);
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public int describeContents() {
		return 0;
	}

}
