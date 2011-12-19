package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemId;

/**
 * Used to adapt UI to the type of item being viewed, should do reasonably safe guesses.
 */
public interface CmsItemClassification {

	/**
	 * @return true if the item is a graphics format supported by the CMS.
	 */
	boolean isGraphic(CmsItemId item);
	
	/**
	 * @return true if the item is xml or an xml-related format supported by the Editor.
	 */
	boolean isXml(CmsItemId item);
	
	/**
	 * @return true if the item is a publishable document, see ticket:343, TODO.
	 */
	boolean isPublishable(CmsItemId item);
	
}
