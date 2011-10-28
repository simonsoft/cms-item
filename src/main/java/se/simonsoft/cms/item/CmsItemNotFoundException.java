package se.simonsoft.cms.item;


/**
 * Thrown when communication succeeds but the item is not found.
 * 
 * Declared as a checked exception although this is not really recoverable,
 * because the CMS is a multi-user environment where this type of error is normal
 * due to the time gap between user input and operation execution.
 * 
 * Services that rely on indexed data might want to retry a few times before
 * throwing this error as it may occur because of indexing delays.
 */
public class CmsItemNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private CmsItemId id;

	public CmsItemNotFoundException(CmsItemId id) {
		super("Not found: " + id);
		this.id = id;
	}
	
	public CmsItemId getId() {
		return id;
	}

}
