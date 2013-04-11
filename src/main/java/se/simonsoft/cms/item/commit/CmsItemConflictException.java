package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Thrown when a committed item's revision in repository does not match the "base" revision.
 * 
 * Indicates modification by someone else since the operation started
 * (i.e. since the base revision was fetched).
 *
 * Error message at commit is something like:
 * svn: E160024: resource out of date; try updating
 * svn: E175002: CHECKOUT of '/svn/demo1/!svn/ver/157/my/file.txt': 409 Conflict (http://host)
 */
public class CmsItemConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private CmsRepository repository;
	private CmsItemPath path;
	private RepoRevision base;

	public CmsItemConflictException(CmsRepository repository, CmsItemPath path, RepoRevision base) {
		super("Detected conflict at '" + path + "'. Item has changed since base reivision " + base.getNumber());
		this.repository = repository;
		this.path = path;
		this.base = base;
	}

	public CmsRepository getRepository() {
		return repository;
	}

	public CmsItemPath getPath() {
		return path;
	}

	public RepoRevision getBase() {
		return base;
	}	
	
}
