package ca.ucalgary.edu.ensf380;

import java.util.Date;

/**
 * The Advertisement class represents an advertisement in the system. Each
 * advertisement has a title, subtitle, text, media path, start date, and end
 * date. The class provides getters for these properties and a method to
 * represent the advertisement as a string.
 *
 * @author Tonmoy, Owen
 * @version 1.0
 * @since 2023-08-06
 */
public class Advertisement {
	// Title of the advertisement
	private String title;

	// Subtitle of the advertisement
	private String subtitle;

	// Text of the advertisement
	private String text;

	// Path to the media file associated with the advertisement
	private String mediaPath;

	// Start date of the advertisement
	private Date startDate;

	// End date of the advertisement
	private Date endDate;

	/**
	 * Constructs an Advertisement with the specified title, subtitle, text, media
	 * path, start date, and end date.
	 *
	 * @param title     The title of the advertisement.
	 * @param subtitle  The subtitle of the advertisement.
	 * @param text      The text of the advertisement.
	 * @param mediaPath The path to the media file associated with the
	 *                  advertisement.
	 * @param startDate The start date of the advertisement.
	 * @param endDate   The end date of the advertisement.
	 */
	public Advertisement(String title, String subtitle, String text, String mediaPath, Date startDate, Date endDate) {
		this.title = title;
		this.subtitle = subtitle;
		this.text = text;
		this.mediaPath = mediaPath;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// Getter methods

	/**
	 * Returns the title of the advertisement.
	 *
	 * @return A string representing the title of the advertisement.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the subtitle of the advertisement.
	 *
	 * @return A string representing the subtitle of the advertisement.
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * Returns the text of the advertisement.
	 *
	 * @return A string representing the text of the advertisement.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns the media path of the advertisement.
	 *
	 * @return A string representing the media path of the advertisement.
	 */
	public String getMediaPath() {
		return mediaPath;
	}

	/**
	 * Returns the start date of the advertisement.
	 *
	 * @return A Date object representing the start date of the advertisement.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Returns the end date of the advertisement.
	 *
	 * @return A Date object representing the end date of the advertisement.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Returns a string representation of the advertisement.
	 *
	 * @return A string representation of the advertisement.
	 */
	@Override
	public String toString() {
		return "Title: " + title + "\nSubtitle: " + subtitle + "\nText: " + text + "\nMedia Path: " + mediaPath
				+ "\nStart Date: " + startDate + "\nEnd Date: " + endDate;
	}
}
