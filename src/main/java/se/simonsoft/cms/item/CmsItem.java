/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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
package se.simonsoft.cms.item;

import java.io.IOException;
import java.io.OutputStream;

import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Models a specific revision of a file or folder entry in the CMS.
 * 
 * The interfaces in this package are provided as a means of handling CMS contents
 * that is independent of retrieval method. Data could originate from repository access,
 * working copy, index, cache etc. and might be fetched as needed instead of at instantiation.
 */
public interface CmsItem {

	CmsItemId getId();

	/**
	 * Item "kind" can not be identified from {@link #getId()}.
	 * @return file or folder or special kinds
	 */
	CmsItemKind getKind();
	
	/**
	 * @return Item status value, arbitrary value (enum defined per customer), null if not set
	 */
	String getStatus();	
	
	/**
	 * @return contents checksum
	 */
	Checksum getChecksum();
	
	/**
	 * @return all versioned properties on this item
	 */
	CmsItemProperties getProperties();
	
	/**
	 * Opens a connection to a file and writes its content to a stream.
	 * @param receiver accepts contents of arbitrary length at arbitrary speed
	 * @return access to contents, opened and closed by caller
	 * @throws {@link se.simonsoft.cms.item.info.CmsConnectionException} if repository connection failed
	 * @throws {@link se.simonsoft.cms.item.info.CmsItemNotFoundException} if the item has disappeard after the {@link CmsItem} instance was returned
	 * @throws {@link se.simonsoft.cms.item.info.CmsTransferException} instead of checked IOException and the like
	 * @throws IOException if transport failed
	 * @throws UnsupportedOperationException if item is folder or backend does not support content reading
	 */
	void getContents(OutputStream receiver) throws UnsupportedOperationException;
	
}
