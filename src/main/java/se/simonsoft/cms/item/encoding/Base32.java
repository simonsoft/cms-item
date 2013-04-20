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
package se.simonsoft.cms.item.encoding;
import java.security.SecureRandom;
import java.util.HashMap;

/**
 * Sortable encoding of numbers to ascii-only strings, understandable in hand writing.
 * Using "Crockford's Base32" as defined in Wikipedia.
 * Inspired by http://code.google.com/p/geospatialweb/source/browse/trunk/geohash/src/Base32.java?r=103
 */
public class Base32 {
	
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
			'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' };

	final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
	static {
		int i = 0;
		for (char c : digits)
			lookup.put(c, i++);
	}
	
	private SecureRandom r = null;

	/**
	 * @deprecated Use instance method 
	 */
	public static String base32(long i) {
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);
		if (!negative)
			i = -i;
		while (i <= -32) {
			buf[charPos--] = digits[(int) (-(i % 32))];
			i /= 32;
		}
		buf[charPos] = digits[(int) (-i)];
		if (negative)
			buf[--charPos] = '-';
		return new String(buf, charPos, (65 - charPos));
	}
	
	/**
	 * @deprecated Use instance method
	 */
	public static long debase32(String base32) {
		
		long result = 0;
		long negative = 1;
		
		String base32Abs = base32;
		if (base32.startsWith("-")) {
			negative = -1;
			base32Abs = base32.substring(1);
		}
		
		for (int i=0; i<base32Abs.length(); i++) {
			result = result * 32;
			Character c = base32Abs.charAt(i);
			Integer ci = lookup.get(c);
			if (ci == null) {
				throw new IllegalArgumentException("invalid base32: " + base32);
			}
			result = result + ci;
		}
		
		return result * negative;
	}
	
	public String encode(long i) {
		return base32(i);
	}
	
	public long decode(String base32) {
		return debase32(base32);
	}
	
	/**
	 * @return The highest sorted encode character
	 */
	public char getZero() {
		return digits[0];
	}
	
	/**
	 * Pads initial {@link #getClass()} 
	 * @param i
	 * @param length
	 * @return
	 * @throws IllegalArgumentException if the encoded number is longer than length
	 */
	public String encodePad(long i, int length) throws IllegalArgumentException {
		String s = base32(i);
		if (s.length() > length) {
			throw new IllegalArgumentException("Encoded string " + s + " from value " + i + " is longer than required " + length);
		}
		while (s.length() < length) {
			s = getZero() + s;
		}
		return s;
	}
	
	/**
	 * @param length
	 * @return A random string with this encoding's characters
	 */
	public String random(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Invalid length " + length);
		}
		if (length == 0) {
			return "";
		}
		if (r == null) {
			r = new SecureRandom();
		}
		return base32(r.nextInt(32)) + random(length - 1);
	}
	
	/**
	 * 
	 * @param i
	 * @param length
	 * @return The number encoded and then padded to length by appending (to the right)
	 *  a {@link #random(int)} for the remaining length, if any
	 * @throws IllegalArgumentException if the encoded number is longer than length
	 */
	public String encodePadRightRandom(long i, int length) throws IllegalArgumentException {
		String s = base32(i);
		if (s.length() > length) {
			throw new IllegalArgumentException("Encoded string " + s + " from value " + i + " is longer than required " + length);
		}
		return s + random(length - s.length());
	}
	
}
