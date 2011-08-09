package org.kth.cos.android.sw.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
	private String text;
	private String postedBy;
	private Date postedAt;

	public Comment(String text, String postedBy, String postedAtStr) throws ParseException {
		super();
		this.text = text;
		this.postedBy = postedBy;
		this.postedAt = parseDate(postedAtStr);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
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

}
