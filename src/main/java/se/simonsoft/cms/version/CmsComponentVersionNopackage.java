package se.simonsoft.cms.version;

/**
 * Version values when there are no manifests at runtime, such as when running in IDE.
 */
public final class CmsComponentVersionNopackage implements CmsComponentVersion {

	@Override
	public boolean isKnown() {
		return false;
	}

	@Override
	public String getBuildName() {
		return null;
	}

	@Override
	public Integer getBuildNumber() {
		return null;
	}

	@Override
	public Integer getSourceRevision() {
		return null;
	}

	@Override
	public String getBuildTag() {
		return DEFAULT_NAME;
	}
	
}
