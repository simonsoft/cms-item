package se.simonsoft.cms.item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Represents a single repository-wide revision identifier that can be used to identify a baseline.
 * In the Subversion world this is a number that is increased for every commit.
 */
public class RepoRevision {

	private static final DateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	static {
		ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private long number;
	private Date date;

	public RepoRevision(long revisionNumber, Date revisionTimestamp) {
		this.number = revisionNumber;
		this.date = revisionTimestamp;
	}
	
	/**
	 * @return The commit revision number.
	 */
	public long getNumber() {
		return this.number;
	}
	
	/**
	 * An intrinsic property of a version control repository is that it has a well defined
	 * state at any historic timestamp.
	 * For a decentralized VCS one also has to know which copy of the repository it refers to.
	 * @return The technology-agnostic way of representing the baseline, UTC timestamp.
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * @return ISO 8601 formatted date, no millis, UTC time without timezone identifyer, append Z to clarify UTC
	 */
	public String getDateIso() {
		return ISO_FORMAT.format(getDate());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + getNumber() + "," + getDateIso(); 
	}
	
}
