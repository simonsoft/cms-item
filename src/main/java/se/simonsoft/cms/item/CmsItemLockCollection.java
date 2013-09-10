package se.simonsoft.cms.item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import se.simonsoft.cms.item.info.CmsLockQuery;

public abstract class CmsItemLockCollection implements Serializable {


	private static final long serialVersionUID = 1L;
	private CmsRepository repository;
	private Map<CmsItemPath, CmsItemLock> map = new HashMap<CmsItemPath, CmsItemLock>();
	
	
	public CmsItemLockCollection(CmsRepository repository) {
		
		this.repository = repository;
	}
	
	
	/** Searches the lock collection for a lock on the object. 
	 * A revision on the item id is disregarded, i.e. returns locks even for historical objects.
	 * @param itemId
	 * @return The lock or null if none is known.
	 */
	public CmsItemLock getLocked(CmsItemId itemId) {
		
		if (repository.equals(itemId.getRepository())) {
			throw new IllegalArgumentException("requested itemId does not match CmsRepository");
		}

		return map.get(itemId.getRelPath());
	}
	
	/** Searches the lock collection for a lock on the object. 
	 * @param path
	 * @return The lock or null if none is known.
	 */
	public CmsItemLock getLocked(CmsItemPath path) {
		
		return map.get(path);
	}
	
	
	/**
	 * Returns items that are currently locked.
	 * @param query Filtering
	 */
	CmsItemLockCollection getLocked(CmsLockQuery query) {
		throw new UnsupportedOperationException("Lock query filtering not implemented");
	}
	
}
