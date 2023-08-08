package ca.ucalgary.edu.ensf380;

public class Advertisement {
	private String title;
	private String subtitle;
	private String text;
	private String mediaPath;
	// Add other fields...

	// Constructor
	public Advertisement(String title, String subtitle, String text, String mediaPath) {
		this.title = title;
		this.subtitle = subtitle;
		this.text = text;
		this.mediaPath = mediaPath;
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

	@Override
	public String toString() {
		return "Title: " + title + "\nSubtitle: " + subtitle + "\nText: " + text + "\nMedia Path: " + mediaPath;
	}
}
