package se.simonsoft.cms.item.commit;

import java.io.InputStream;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

public class FileAdd extends CmsCommitChangeBase {

	private CmsItemPath path;
	private RepoRevision baseRevision;
	private InputStream contents;

	/**
	 * 
	 * @param path
	 * @param parentFolderBaseRevision
	 * @param contents Will be opened and closed when the item is processed
	 */
	public FileAdd(CmsItemPath path,
			RepoRevision parentFolderBaseRevision,
			InputStream contents) {
		this.path = path;
		this.baseRevision = parentFolderBaseRevision;
		this.contents = contents;
	}
	
	@Override
	protected StatContent getStatContents() {
		return StatContent.A;
	}

	@Override
	protected StatProps getStatProps() {
		return StatProps._;
	}

	@Override
	protected boolean isCopy() {
		return false;
	}	
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public RepoRevision getBaseRevision() {
		return baseRevision;
	}

	/**
	 * @return Not yet opened stream
	 */
	public InputStream getContents() {
		return contents;
	}

}
