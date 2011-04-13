package se.simonsoft.cms.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

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
	
	@Override
	public String getHex(Algorithm algorithm) {
		return toHex(getDigest(algorithm).digest());
	}

	/**
	 * Adds content to the current checksum values.
	 * @param source content
	 * @return this instance for chaining
	 * @throws IOException if source reading failed
	 */
	public ChecksumRead add(InputStream source) throws IOException {
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
