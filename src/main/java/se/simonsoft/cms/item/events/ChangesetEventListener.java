package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.events.change.CmsChangeset;

public interface ChangesetEventListener {

	/**
	 * Post commit notification.
	 * 
	 * @param changeset
	 */
	public void onCommit(CmsChangeset changeset);
	
}
