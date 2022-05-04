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

public class CmsLabelVersion implements CmsLabel, Comparable<CmsLabelVersion> {

	private List<String> segments;
	private List<String> prerelease;
	
	private static final String PAD = "0000000000";
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
			this.prerelease = null;
		}
		
	}
	
	@Override
	public String getLabel() {
		return String.join(".", segments) + (prerelease == null ? "" : "-" + String.join(".", prerelease)); 
	}

	public String getLabelSort() {
		// Always add the VERSION_SUFFIX to ensure sorting a release after a prerelease.
		return String.join(".", getVersionSegmentsSort()) + (prerelease == null ? RELEASE_SUFFIX : PRERELEASE_PREFIX + String.join(".", getPrereleaseSegmentsSort())); 
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
			return getSortable(segments, true);
		} else {
			return Stream.concat(getSortable(segments, true).stream(), getSortable(prerelease, false).stream()).collect(Collectors.toList());
		}
	}

	
	public List<String> getVersionSegments() {
		// Already immutable.
		return this.segments;
	}

	public List<String> getVersionSegmentsSort() {
		return getSortable(this.segments, true);
	}
	
	
	public List<String> getPrereleaseSegments() {
		// Already immutable.
		return this.prerelease;
	}

	public List<String> getPrereleaseSegmentsSort() {
		return getSortable(this.prerelease, false);
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
	
	
	private static List<String> getSortable(List<String> segments, boolean padAll) {
		ArrayList<String> l = new ArrayList<String>(segments.size());
		for (String s: segments) {
			if (padAll) {
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
