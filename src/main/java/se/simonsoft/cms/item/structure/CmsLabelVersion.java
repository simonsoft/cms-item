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

public class CmsLabelVersion implements CmsLabel, Comparable<CmsLabelVersion> {

	private List<String> segments;
	
	private static final String PAD = "0000000000";
	
	
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
		
		// Creates an immutable list.
		this.segments = List.of(label.split("\\."));
	}
	
	@Override
	public String getLabel() {
		return String.join(".", segments);
	}

	public String getLabelSort() {
		return String.join(".", getSegmentsSort());
	}
	
	public List<String> getSegments() {
		// Already immutable.
		return this.segments;
	}
	
	public List<String> getSegmentsSort() {
		ArrayList<String> l = new ArrayList<String>(this.segments.size());
		for (String s: this.segments) {
			l.add(getSortable(s));
		}
		return Collections.unmodifiableList(l);
	}
	
	@Override
	public String toString() {
		return this.getLabel();
	}
	
	@Override
	public int hashCode() {
		return getLabel().hashCode();
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
