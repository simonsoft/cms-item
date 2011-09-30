package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;

/**
 * Concrete class for use in the common case when a logical id
 * is provided as string argument to an operation.
 * Mutable: allows adding of hostname and peg revision if not already set.
 * Provides a String argument constructor for use as JAX-RS parameter.
 */
public class CmsItemIdArg implements CmsItemId {

	/**
	 * @param logicalId with or without hostname and peg rev
	 */
	public CmsItemIdArg(String logicalId) {
		
	}
	
	public boolean isFullyQualified() {
		throw new UnsupportedOperationException("Method not implemented");
	}
	
	public boolean isFullyQualifiedOriginally() {
		throw new UnsupportedOperationException("Method not implemented");
	}
	
	/**
	 * Using and is-name rather than has-name to conform with javabean spec.
	 * @return true if id has peg rev
	 */
	public boolean isPegged() {
		throw new UnsupportedOperationException("Method not implemented");
	}
	
	public boolean isPeggedOriginally() {
		throw new UnsupportedOperationException("Method not implemented");
	}
	
	/**
	 * @param fullyQualifiedName Often set by the web application and not the caller
	 * when the caller is on the same server.
	 */
	public void setHostname(String fullyQualifiedName) {
		
	}
	
	public void setPegRev(long pegRev) {
		
	}
	
	/**
	 * Used for validation in services that support only operations in
	 * a current repository connection session.
	 */
	@Override
	public String getRepositoryUrl() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}
	
	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public Long getPegRev() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String getLogicalId() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public String getLogicalIdFull() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public CmsItemPath getRelPath() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}

}
