package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.CmsRepository;

class CmsRepositoryHostnameUnknown extends CmsRepository {

	private String logicalId;

	public CmsRepositoryHostnameUnknown(String parentPath, String name, String logicalId) {
		super(null, null, parentPath, name);
		this.logicalId = logicalId;
	}

	@Override
	public String getHost() {
		throw new IllegalStateException("Hostname unknown for " + logicalId);
	}

	@Override
	public String getHostname() {
		throw new IllegalStateException("Hostname unknown for " + logicalId);
	}

	@Override
	public String getServerRootUrl() {
		throw new IllegalStateException("Hostname unknown for " + logicalId);
	}

	@Override
	public String getUrl() {
		throw new IllegalStateException("Hostname unknown for " + logicalId);
	}
	
}
