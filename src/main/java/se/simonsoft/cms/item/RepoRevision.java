/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a single repository-wide revision identifier that can be used to identify a baseline.
 * In the Subversion world this is a number that is increased for every commit.
 * 
 * The accompanying timestamp is the universal method of representing a baseline
 * in a centralized version control system.
 * URL + timestamp identifies a baseline in a DVCS.
 * 
 * The timestamp also provides the most human readable form of identification,
 * formatted either as a date and time or as "X [unit] ago"
 * (ok, small svn revision numbers might also be easy to read).
 * 
 * Note that Subversion allows arbitrary timestamps on revisions.
 * Repoisitories can for example be combined from different dump files,
 * so newer revisions have older dates. Thus the revision number will be the baseline,
 * but the date on a revision shows when the commit was made.
 * 
 * File systems also allow abitrary modification of time stamps.
 * It should be assumed that the underlying storage is well managed. 
 */
public class RepoRevision {

	private static final Logger logger = LoggerFactory.getLogger(RepoRevision.class);
	
	private static final DateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	static {
		ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private static final long DATE_ONLY_MIN = 631152000000L; // 1990-01-01
	
	private boolean numberIsTimestamp = false;
	private long number;
	private Date date;

	public RepoRevision(long revisionNumber, Date revisionTimestamp) {
		this.number = revisionNumber;
		this.date = revisionTimestamp;
	}

	protected RepoRevision(Date revisionTimestamp) {
		this(revisionTimestamp.getTime(), revisionTimestamp);
		if (revisionTimestamp.getTime() < DATE_ONLY_MIN) {
			throw new IllegalArgumentException("Date-only revision not allowed before " 
					+ ISO_FORMAT.format(DATE_ONLY_MIN) + ", got " + ISO_FORMAT.format(revisionTimestamp));
		}
		this.numberIsTimestamp = true;
	}
	
	/**
	 * Returns the ordinal of the commit, with a later commit always having a higher number.
	 * 
	 * Note that in subversion the {@link #getDate()} could actually be out of sequence,
	 * because of revprop changes or repositories combined through multiple dump files.
	 * 
	 * For backends with no native ordinal this method can return the timestamp's milliseconds value. 
	 * 
	 * @return The commit revision number
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

	/**
	 * @return Revision in backend's native format
	 */
	@Override
	public String toString() {
		if (numberIsTimestamp) {
			return getDateIso();
		}
		return Long.toString(number); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof RepoRevision)) return false;
		RepoRevision r = (RepoRevision) obj;
		if (number != r.getNumber()) return false;
		if (date == null) {
			logger.warn("Comparing revision {} that lacks timestamp with {}", getNumber(), r);
			if (r.getDate() != null) return false;
		} else if (r.getDate() == null) {
			logger.warn("Comparing {} with revision that lacks timestamp {}", this, r.getNumber());
			return false;
		} else {
			if (!date.equals(r.getDate())) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return (int) (getNumber() % Integer.MAX_VALUE);
	}
	
}
