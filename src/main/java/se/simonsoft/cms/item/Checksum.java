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
package se.simonsoft.cms.item;

/**
 * Represents the different checksums supported in CMS.
 */
public interface Checksum {

	/**
	 * Digest algorithm names.
	 */
	public enum Algorithm {
		MD5,
		SHA1,
		SHA256;
		/**
		 * @return name used in {@link java.security.MessageDigest}
		 */
		public String getJavaName() {
			if (this == SHA256) return "SHA-256";
			return super.toString();
		}
	}
	
	/**
	 * Default algorithm set if nothig else is specified.
	 */
	public static final Algorithm[] DEFAULT = new Algorithm[]{ Algorithm.MD5, Algorithm.SHA1 };

	/**
	 * @param algorithm a digest method
	 * @return true if the checksum instance supports the given algorithm digest
	 */
	public boolean has(Algorithm algorithm);
	
	/**
	 * @param algorithm a digest method
	 * @return the hex representation of the checksum value
	 */
	public String getHex(Algorithm algorithm);
	
	/**
	 * @return {@link #getHex(Algorithm)} {@link Algorithm#MD5}
	 */
	public String getMd5() throws UnsupportedOperationException;
	
	/**
	 * @return {@link #getHex(Algorithm)} {@link Algorithm#SHA1}
	 */	
	public String getSha1() throws UnsupportedOperationException;
	
	/**
	 * @param obj other checksum
	 * @return true if the checksums has identical set of algorithms and all of them, at least 1, match
	 */
	public boolean equals(Object obj);
	
	/**
	 * @param obj other checksum
	 * @return true if at least one algorithm is used in both objects and all used in both match
	 */
	public boolean equalsKnown(Checksum obj);
	
}
