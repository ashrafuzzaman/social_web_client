package org.kth.cos.android.sw.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;

public class Comment {
	private String text;
	private String postedBy;
	private int sequenceNumber;
	private Date postedAt;

	public Comment(String text, String postedBy, String postedAtStr, String sequenceNumberStr) throws ParseException {
		super();
		this.text = text;
		this.postedBy = postedBy;
		this.postedAt = parseDate(postedAtStr);
		this.sequenceNumber = Integer.parseInt(sequenceNumberStr);
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
	
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
