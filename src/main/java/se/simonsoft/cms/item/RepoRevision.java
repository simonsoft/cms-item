package se.simonsoft.cms.item;

import java.util.Date;

/**
 * Represents a single repository-wide revision identifier that can be used to identify a baseline.
 * In the Subversion world this is a number that is increased for every commit.
 */
public class RepoRevision {

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
	
}
