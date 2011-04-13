package se.simonsoft.cms.item;

import se.simonsoft.cms.item.Checksum.Algorithm;

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
	 * Might some day be relaxed with rules on what we need to consider files identical.
	 * 
	 * This method aims for usefulness in collections, allowing different Checksum
	 * implementations (i.e. from files, indexes, svn server etc).
	 * Implementations that really require all stored values to be identical should
	 * compare the checksums one by one.
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}	

}