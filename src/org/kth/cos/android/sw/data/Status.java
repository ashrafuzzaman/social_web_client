package org.kth.cos.android.sw.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Status {
	private String value;
	private String profileName;
	private String postedBy;
	private Date postedAt;

	public Status() {
	}

	public Status(String value, String profileName, String postedAtStr) throws ParseException {
		this.value = value;
		this.profileName = profileName;
		this.postedAt = parseDate(postedAtStr);
	}

	public Status(String value, String profileName, String postedBy, String postedAtStr) throws ParseException {
		this.value = value;
		this.profileName = profileName;
		this.postedAt = parseDate(postedAtStr);
		this.postedBy = postedBy;
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

}
