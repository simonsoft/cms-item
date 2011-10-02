package se.simonsoft.cms.item.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;

/**
 * Concrete class for use in the common case when a logical id
 * is provided as string argument to an operation.
 * 
 * Mutable: allows adding of hostname and peg revision if not already set.
 * 
 * Provides a String argument constructor for use as JAX-RS parameter.
 * 
 * This class never needs to deal with encoding of logical ID, which is tricky,
 * only decoding of path, which is simple. Assuming hostname is always ascii.
 */
public class CmsItemIdArg implements CmsItemId {

	public static final String PROTO = "x-svn://";
	public static final String HTTP = "http://";
	public static final String PEG = "?p=";
	public static final Pattern NICE = Pattern.compile(PROTO + "([^/]*)(.*/)([^^]*)\\^([^?]+)(?:\\?p=(\\d+))?");
	private String host = "";
	private String parent;
	private String repo;
	private String relpath;
	private Long pegRev = null;
	private boolean orgfull;
	private boolean orgpeg;
	
	/**
	 * @param logicalId with or without hostname and peg rev
	 */
	public CmsItemIdArg(String logicalId) {
		Matcher m = NICE.matcher(logicalId);
		if (!m.matches()) {
			throw new IllegalArgumentException("Not a valid logical id: " + logicalId);
		}
		this.host = m.group(1);
		this.parent = m.group(2);
		this.repo = m.group(3);
		this.relpath = m.group(4);
		if (m.group(5) != null) {
			this.pegRev  = Long.parseLong(m.group(5));
			this.orgpeg = true;
		}
		if (isFullyQualified()) {
			this.orgfull = true;
		}
	}
	
	public boolean isFullyQualified() {
		return host.length() > 0;
	}
	
	public boolean isFullyQualifiedOriginally() {
		return orgfull;
	}
	
	/**
	 * Using an is-name rather than has-name to conform with javabean spec.
	 * @return true if id has peg rev
	 */
	public boolean isPegged() {
		return pegRev != null;
	}
	
	public boolean isPeggedOriginally() {
		return orgpeg;
	}
	
	/**
	 * @param fullyQualifiedName Often set by the web application and not the caller
	 * when the caller is on the same server.
	 */
	public void setHostname(String fullyQualifiedName) {
		if (isFullyQualified()) {
			throw new IllegalStateException("Hostname already set for " + getLogicalIdFull());
		}
		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0) {
			throw new IllegalArgumentException("Method not intended for removing hostname");
		}
		host = fullyQualifiedName;
	}
	
	/**
	 * Makes sure hostname is set and matching a given value.
	 * @param fullyQualifiedName hostname to set or to expect
	 * @throws IllegalArgumentException If the hostname of the logicalId does not match the given
	 */
	public void setHostnameOrValidate(String fullyQualifiedName)
			throws IllegalArgumentException {
		if (isFullyQualified()) {
			if (!host.equals(fullyQualifiedName))  {
				throw new IllegalArgumentException("Unexpected hostname in " + getLogicalIdFull() + ", expected " + fullyQualifiedName);
			}
		} else {
			setHostname(fullyQualifiedName);
		}
	}
	
	/**
	 * @param pegRev when given as an optional parameter alongside the id
	 */
	public void setPegRev(long pegRev) {
		if (isPegged()) {
			throw new IllegalStateException("Peg revision already set for " + getLogicalIdPath());
		} else {
			this.pegRev = pegRev;
		}
	}
	
	/**
	 * Used for validation in services that support only operations in
	 * a current repository connection session.
	 */
	@Override
	public String getRepositoryUrl() {
		if (!isFullyQualified()) {
			throw new IllegalStateException("Hostname unknown for " + getLogicalId());
		}
		return HTTP + host + parent + repo;
	}

	private String getQuery() {
		if (isPegged()) {
			return PEG + pegRev;
		}
		return "";
	}

	private String getLogicalIdPath() {
		return PROTO + parent + repo + "^" + relpath;
	}
	
	@Override
	public String getUrl() {
		return getRepositoryUrl() + relpath;
	}

	@Override
	public Long getPegRev() {
		return pegRev;
	}

	/**
	 * Always known in this implementation, unlike {@link #getLogicalIdFull()}.
	 */
	@Override
	public String getLogicalId() {
		return getLogicalIdPath() + getQuery();
	}

	@Override
	public String getLogicalIdFull() {
		if (!isFullyQualified()) {
			throw new IllegalStateException("Hostname unknown for " + getLogicalId());
		}
		return PROTO + host + parent + repo + "^" + relpath + getQuery();
	}

	@Override
	public CmsItemPath getRelPath() {
		String decoded;
		try {
			decoded = URLDecoder.decode(relpath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed to decode with charset UTF-8");
		}
		return new CmsItemPath(decoded);
	}

}
