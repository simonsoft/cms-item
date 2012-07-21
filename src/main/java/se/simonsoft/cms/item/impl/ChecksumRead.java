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
package se.simonsoft.cms.item.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import se.simonsoft.cms.item.Checksum;

/**
 * Reads source once and calculates all enabled checksums.
 * <p>
 * Sample SHA-1 calculation:
 * <code>new ChecksumRead().add(file).getSha1();</code>
 * <p>
 * This is a reasonable default implementation because the CMS uses
 * SHA1 as preferred checksum, so to calculate MD5 even if it is not
 * read will be faster than reading sources twice when MD5 is needed.
 * <p>
 * Use cases that know for sure only one algorithm will be needed
 * should use the constructor
 * <p>
 * Alternative implementations could keep references to backend and
 * read the source only as a particular checksum is requested.
 */
public class ChecksumRead extends ChecksumBase implements Checksum {

	private Map<Algorithm, MessageDigest> d;
	private byte[] buffer = new byte[1024];
	
	private boolean done = false;
	private Map<Algorithm, byte[]> f;
	
	/**
	 * Enables default algorithms, currently all.
	 * The list might grow in the future, if performance is critical use {@link #ChecksumRead(Algorithm...)}.
	 */
	public ChecksumRead() {
		this(Algorithm.values());
	}
	
	public ChecksumRead(Algorithm... algorithms) {
		d = new TreeMap<Algorithm, MessageDigest>();
		for (Algorithm a : algorithms) {
			try {
				d.put(a, MessageDigest.getInstance(a.toString()));
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Failed to initialize checksum algorithm " + a, e);
			}
		}
		f = new TreeMap<Algorithm, byte[]>();
	}
	
	@Override
	public boolean has(Algorithm algorithm) {
		return d.containsKey(algorithm);
	}

	protected MessageDigest getDigest(Algorithm algorithm) throws UnsupportedOperationException {
		if (!has(algorithm)) {
			throw new UnsupportedOperationException("Checksum " + algorithm + " not supported here");
		}
		return d.get(algorithm);
	}

	/**
	 * Guards access to {@link MessageDigest#digest()} because it resets value.
	 * @throws UnsupportedOperationException
	 */
	protected byte[] getDigestFinal(Algorithm algorithm) throws UnsupportedOperationException {
		done = true;
		if (!f.containsKey(algorithm)) {
			f.put(algorithm, getDigest(algorithm).digest());
		}
		return f.get(algorithm);
	}
	
	@Override
	public String getHex(Algorithm algorithm) {
		return toHex(getDigestFinal(algorithm));
	}

	/**
	 * Adds content to the current checksum values.
	 * @param source content
	 * @return this instance for chaining
	 * @throws IOException if source reading failed
	 */
	public ChecksumRead add(InputStream source) throws IOException {
		if (done) {
			throw new IllegalStateException("No more content allowed because checksum has been read");
		}
		int numRead;
		do {
			numRead = source.read(buffer);
			if (numRead > 0) {
				for (MessageDigest sum : d.values()) {
					sum.update(buffer, 0, numRead);
				}
			}
		} while (numRead != -1);
		source.close();
		return this;
	}
	
	public ChecksumRead add(File sourceFile) throws FileNotFoundException, IOException {
		return add(new FileInputStream(sourceFile));
	}


	// http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-le
	private String toHex(byte[] bytes) {
		java.math.BigInteger bi = new java.math.BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi).toLowerCase();
	}

}
