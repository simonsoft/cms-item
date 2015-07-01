package se.simonsoft.cms.item.events.change;

import java.util.Comparator;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Compare two CmsChangesetItem based on their CmsItemPath.
 * Repository root (null) sorts at the top.
 *
 */
public class CmsChangesetItemComparator implements Comparator<CmsChangesetItem> {


	@Override
	public int compare(CmsChangesetItem o1, CmsChangesetItem o2) {
		
		CmsItemPath p1 = o1.getPath();
		CmsItemPath p2 = o2.getPath();
		
		if (p1 == null && p2 == null) {
			return 0;
		}
		if (p1 == null) {
			return -1;
		}
		if (p2 == null) {
			return 1;
		}
		return p1.compareTo(p2);
	}

}
