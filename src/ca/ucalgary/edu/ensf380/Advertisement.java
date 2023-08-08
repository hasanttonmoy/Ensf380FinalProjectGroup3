package ca.ucalgary.edu.ensf380;

import java.util.Date;

public class Advertisement {
	private String title;
	private String subtitle;
	private String text;
	private String mediaPath;
	private Date startDate;
	private Date endDate;

	// Constructor
	public Advertisement(String title, String subtitle, String text, String mediaPath, Date startDate, Date endDate) {
		this.title = title;
		this.subtitle = subtitle;
		this.text = text;
		this.mediaPath = mediaPath;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// Getters
	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getText() {
		return text;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return "Title: " + title + "\nSubtitle: " + subtitle + "\nText: " + text + "\nMedia Path: " + mediaPath
				+ "\nStart Date: " + startDate + "\nEnd Date: " + endDate;
	}
}
