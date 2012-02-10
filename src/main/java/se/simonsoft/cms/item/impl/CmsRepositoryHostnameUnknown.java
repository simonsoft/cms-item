package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.CmsRepository;

@Deprecated // For 1.9, 
class CmsRepositoryHostnameUnknown extends CmsRepository {

	private String logicalId;

	public CmsRepositoryHostnameUnknown(String parentPath, String name, String logicalId) {
		super(null, null, parentPath, name);
		if (logicalId == null || logicalId.indexOf('^') < 0) {
			throw new IllegalArgumentException("Logical ID must contain root marker. Got " + this.logicalId);
		}
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

	@Override
	public String toString() {
		return logicalId.substring(0, logicalId.indexOf('^') + 1);
	}
	
}
