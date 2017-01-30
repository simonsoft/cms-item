package se.simonsoft.cms.item.export;

/**
 * The name of a single metadata item.
 *
 */
public class CmsExportMetaKey {

	private final String key;
	
	public CmsExportMetaKey(String key) {
		
		// TODO Validate null.
		// TODO Validate key with regex, see CmsItemArg.
		
		this.key = key;
	}

	public String getKey() {
		
		return this.key;
	}
	
	@Override
	public boolean equals(Object anObject) {
		return this.key.equals(anObject);
	}
	
	@Override
	public int hashCode() {
		return this.key.hashCode();
	}
	
	@Override
	public String toString() {
		return this.key.toString();
	}
	
}
