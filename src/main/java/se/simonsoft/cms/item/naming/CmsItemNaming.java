package se.simonsoft.cms.item.naming;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Created by jonand on 17/02/16.
 */
public interface CmsItemNaming {

    CmsItemPath getItemPath(CmsItemPath folder, CmsItemNamePattern namePattern, String extension);
}
