/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Identifies a version / release attempting to support both simple series and Semantic Versioning (SemVer).
 *  - Supports sort ordering both simple series and Semantic Versioning, where multiple identifiers are separated by '.'.
 *  - Characters are expected to stay within: a-z, A-Z, 0-9, full stop, hyphen and underscore.
 *  - The label must not exceed 20 characters in length.
 *  - Each version identifier (separated by '.') is sorted individually. 
 *  - Both alpha and numeric identifiers are sorted as a series, e.g.: 2 < 10 < A < B < AA < BA < a < b < aa < ba
 * 
 * Prerelease identifiers are handled very similar to the SemVer 2.0.0 specification.
 *  - Identifiers following the last hyphen are considered pre-release identifiers.
 *  - Pre-release identifiers consisting of only digits are sorted numerically.
 *  - Pre-release identifiers with letters are sorted lexically (different than release identifiers).
 *  
 *  Additional release identifiers vs prerelease identifier should be used to indicate their respective meaning.
 *  - An additional release identifier indicates a more mature release.
 *  - A prerelease identifier indicated a less mature version than the upcoming release.
 *  - E.g. A.3 < A.4-beta < A.4 < A.4.0 < A.4.1 
 *
 * https://semver.org/spec/v2.0.0.html
 */
public class CmsLabelVersion implements CmsLabel, Comparable<CmsLabelVersion> {

	private List<String> segments;
	private List<String> prerelease;
	
	private static final List<String> EMPTY = Collections.unmodifiableList(new ArrayList<String>());
	
	private static final String PAD_NUM = "//////////"; // Slash precedes 0 in ASCII.
	private static final String PAD_AUP = "@@@@@@@@@@"; // at precedes A in ASCII.
	private static final String PAD_ALO = "``````````"; // accent precedes a in ASCII.
	private static final String RELEASE_SUFFIX = "-";
	private static final String PRERELEASE_PREFIX = "+";
	
	
	public CmsLabelVersion(String label) {
		if (label == null) {
			throw new IllegalArgumentException("label can not be null");
		}
		if (label.trim().length() == 0) {
			throw new IllegalArgumentException("label can not be an empty string");
		}
		if (label.trim().length() > 20) {
			throw new IllegalArgumentException("label can not exceed 20 characters");
		}
		if (label.trim().startsWith(".")) {
			throw new IllegalArgumentException("label can not start with '.'");
		}
		if (label.trim().endsWith(".")) {
			throw new IllegalArgumentException("label can not end with '.'");
		}
		if (label.trim().contains("..")) {
			throw new IllegalArgumentException("label can not contain consecutive '.'");
		}
		
		if (label.contains("-")) {
			String release = label.substring(0, label.lastIndexOf("-"));
			String pre = label.substring(label.lastIndexOf("-")+1);
			this.segments = List.of(release.split("\\."));
			this.prerelease = List.of(pre.split("\\."));
		
		} else {
			// Creates an immutable list.
			this.segments = List.of(label.split("\\."));
			this.prerelease = EMPTY;
		}
		
	}
	
	@Override
	public String getLabel() {
		return String.join(".", segments) + (prerelease.isEmpty() ? "" : "-" + String.join(".", prerelease)); 
	}

	public String getLabelSort() {
		// Always add the VERSION_SUFFIX to ensure sorting a release after a prerelease.
		return String.join(".", getVersionSegmentsSort()) + (prerelease.isEmpty() ? RELEASE_SUFFIX : PRERELEASE_PREFIX + String.join(".", getPrereleaseSegmentsSort())); 
	}
	
	public List<String> getSegments() {
		if (prerelease == null) {
			return segments;
		} else {
			return Stream.concat(segments.stream(), prerelease.stream()).collect(Collectors.toList());
		}
	}

	public List<String> getSegmentsSort() {
		if (prerelease == null) {
			return getSortable(segments, false);
		} else {
			return Stream.concat(getSortable(segments, false).stream(), getSortable(prerelease, true).stream()).collect(Collectors.toList());
		}
	}

	
	/**
	 * @return list of Release version identifiers (excl prerelease)
	 */
	public List<String> getVersionSegments() {
		// Already immutable.
		return this.segments;
	}

	public List<String> getVersionSegmentsSort() {
		return getSortable(this.segments, false);
	}
	
	/**
	 * @return list of Pre-Release version identifiers (excl identifiers preceeding the last hyphen)
	 */
	public List<String> getPrereleaseSegments() {
		// Already immutable.
		return this.prerelease;
	}

	public List<String> getPrereleaseSegmentsSort() {
		return getSortable(this.prerelease, true);
	}
	
	
	@Override
	public String toString() {
		return this.getLabel();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null && getLabel().equals(obj.toString());
	}
	
	@Override
	public int hashCode() {
		return getLabel().hashCode();
	}
	
	
	private static List<String> getSortable(List<String> segments, boolean prerelease) {
		ArrayList<String> l = new ArrayList<String>(segments.size());
		for (String s: segments) {
			if (!prerelease) {
				l.add(getSortable(s));
			} else if (s.matches("^[0-9]+$")) {
				// prerelease identifiers should only be padded if numerical
				l.add(getSortable(s));
			} else {
				l.add(s);
			}
		}
		return Collections.unmodifiableList(l);
	}
	
	private static String getSortable(String s) {
		// Pad with the first character in each subgroup.
		String PAD = (s.matches("^[0-9]+$") ? PAD_NUM : (s.matches("^[0-9A-Z_]+$") ? PAD_AUP : PAD_ALO));
		if (s.length() > PAD.length()) {
			return s;
		} else {
			return PAD.substring(s.length()) + s;
		}
	}

	@Override
	public int compareTo(CmsLabelVersion o) {
		return this.getLabelSort().compareTo(o.getLabelSort());
	}
}
