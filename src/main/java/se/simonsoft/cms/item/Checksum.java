package se.simonsoft.cms.item;

/**
 * Represents the different checksums supported in CMS.
 */
public interface Checksum {

	/**
	 * Digest algorithm names.
	 */
	public enum Algorithm {
		SHA1,
		MD5
	}

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
	 * @return {@link #getHex(Algorithm)} {@link Algorithm#MD5}
	 */	
	public String getSha1() throws UnsupportedOperationException;
	
	/**
	 * @param obj other checksum
	 * @return true if the checksums has identical set of algorithms and all of them, at least 1, match
	 */
	public boolean equals(Object obj);
	
}
