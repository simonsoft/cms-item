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
package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.Checksum;

/**
 * Shared implementation of equals, hashCode and toString.
 */
public abstract class ChecksumBase implements Checksum {

	public ChecksumBase() {
		super();
	}
	
	@Override
	public String getMd5() {
		return getHex(Algorithm.MD5);
	}

	@Override
	public String getSha1() {
		return getHex(Algorithm.SHA1);
	}	
	
	/**
	 * Very strict equals, requires same response from all {@link #has(se.simonsoft.cms.item.Checksum.Algorithm)}
	 * 
	 * This method aims for usefulness in collections, allowing different Checksum
	 * implementations (i.e. from files, indexes, svn server etc).
	 */
	@Override
	public boolean equals(Object obj) {
		boolean gotone = false;
		boolean gotmd5 = false;
		boolean gotsha1 = false;
		if (obj instanceof Checksum) {
			Checksum o = (Checksum) obj;
			for (Algorithm a : Algorithm.values()) {
				if (this.has(a) && o.has(a)) {
					if (!this.getHex(a).equals(o.getHex(a))) {
						return false;
					}
					gotone = true;
					if (a == Algorithm.MD5) gotmd5 = true;
					if (a == Algorithm.SHA1) gotsha1 = true;
				}
			}
		}
		return gotone && gotmd5 && gotsha1;
	}

	@Override
	public boolean equalsKnown(Checksum obj) {
		for (Algorithm a : Algorithm.values()) {
			if (has(a) && obj.has(a) && getHex(a).equals(obj.getHex(a))) return true;
		}
		return false;
	}	
	
	private String concat(Checksum c) {
		StringBuffer s = new StringBuffer();
		for (Algorithm a : Algorithm.values()) {
			if (c.has(a)) s.append(c.getHex(a));
		}
		return s.toString();
	}

	@Override
	public int hashCode() {
		return concat(this).hashCode();
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (Algorithm a : Algorithm.values()) {
			s.append(',').append(a).append('=');
			if (this.has(a)) {
				s.append(this.getHex(a));
			} else {
				s.append("null");
			}
		}
		return s.substring(1);
	}

}