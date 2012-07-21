/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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

/**
 * Represents a single repository-wide revision identifier that can be used to identify a baseline.
 * In the Subversion world this is a number that is increased for every commit.
 * The accompanying timestamp is the universal method of representing a baseline
 * in a centralized version control system.
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
