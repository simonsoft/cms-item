package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

public class CmsItemLockedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CmsItemLockedException(CmsRepository repository, CmsItemPath path) {
		super("Item is locked at path " + path);
	}
	
	public CmsItemLockedException(CmsRepository repository, CmsItemPath path, CmsItemLock lock) {
		super("Item is locked by user " + lock.getOwner() + " at path " + path);
	}
	
}
