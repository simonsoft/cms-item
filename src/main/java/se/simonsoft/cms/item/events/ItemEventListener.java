package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.events.change.CmsChangeset;
import se.simonsoft.cms.item.events.change.CmsChangesetItem;
import se.simonsoft.cms.item.events.change.CmsChangesetItemFlags;

/**
 * Tentative interface, outlining future event model.
 * 
 * TODO Global, or per repository? Global right?
 * 
 * TODO decide how to handle the difference between pre- and post-notification.
 * 
 * TODO how to filter on repository and path,
 * including items anywhere under a specific folder
 * (see {@link CmsChangeset#affectsIndirectly(CmsItemId)}.
 */
public interface ItemEventListener {

	/**
	 * @return the filter for which events this handler wants to be notified about
	 */
	CmsChangesetItemFlags getItemFilter();
	
	/**
	 * @param revision items only carry commit revision
	 * @param event
	 * @throws ItemChangeReject
	 */
	void onItemChange(RepoRevision revision, CmsChangesetItem event) throws ItemChangeReject;
	
	class ItemChangeReject extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
}
