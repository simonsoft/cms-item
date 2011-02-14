package se.simonsoft.cms.item;

/**
 * Optional base class for {@link CmsItem}, throwing {@link UnsupportedOperationException} in all methods.
 * 
 * Extended by implementations that don't want compilation errors as new methods are added.
 * Also useful for anonymous inner classes in unit tests.
 */
public abstract class CmsItemBase implements CmsItem {

	@Override
	public CmsItemId getId() {
		throw new UnsupportedOperationException("getId not implemented");
	}

	@Override
	public String getStatus() {
		throw new UnsupportedOperationException("getStatus not implemented");
	}

	@Override
	public String getChecksumMd5() {
		throw new UnsupportedOperationException("getChecksumMd5 not implemented");
	}

}
