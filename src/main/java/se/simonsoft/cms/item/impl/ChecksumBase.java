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
	 * Might some day be relaxed with rules on what we need to consider files identical.
	 * 
	 * This method aims for usefulness in collections, allowing different Checksum
	 * implementations (i.e. from files, indexes, svn server etc).
	 * Implementations that really require all stored values to be identical should
	 * compare the checksums one by one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Checksum) {
			String t = concat(this);
			if (t.length() > 0) return t.equals(concat((Checksum) obj));
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