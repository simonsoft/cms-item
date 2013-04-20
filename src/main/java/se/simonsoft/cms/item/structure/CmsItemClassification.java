/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
