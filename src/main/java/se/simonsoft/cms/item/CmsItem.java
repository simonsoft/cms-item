/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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

import java.io.OutputStream;
import java.util.Map;

import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Models a specific revision of a file or folder entry in the CMS.
 * 
 * The interfaces in this package are provided as a means of handling CMS contents
 * that is independent of retrieval method. Data could originate from repository access,
 * working copy, index, cache etc. and might be fetched as needed instead of at instantiation.
 * 
 * Implementations may delay fetching of {@link #getProperties()} and {@link #getContents(OutputStream)}
 * but if the item was instantiated with HEAD revision both properties and contents
 * should be fetched from the revision that was HEAD at the time.
 */
public interface CmsItem {

	/**
	 * @return the ID with which this item is fetced, thus getPegRev can differ from {@link #getRevisionChanged()}
	 */
	CmsItemId getId();
	
	/**
	 * The revision when this item in its current state (contents and properties) was committed.
	 * Note that this could have happened at a different path than current.
	 * @return the commit revision of the fetched item, often called "last changed"
	 */
	RepoRevision getRevisionChanged();
	
	/**
	 * @return the username that produced {@link #getRevisionChanged()}
	 */
	String getRevisionChangedAuthor();
	
	/**
	 * Should we complement getRevisionChanged()?
	 * @return first revision when this item was available at its current path with its current contents
	 */
	//RepoRevision getRevisionPath();
	
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
	 * 
	 * @param cmsClass
	 * @return true if the item is of the specified "class" (case-insensitive)
	 */
	boolean isCmsClass(String cmsClass);
	
	/**
	 * @return contents checksum
	 */
	Checksum getChecksum();
	
	/** Versioned properties on this item, some implementations allow editing.
	 * @return all versioned properties on this item
	 */
	CmsItemProperties getProperties();
	
	/** Read-only key-value metadata for this item, such as embedded metadata.
	 * @return unmodifiable map of metadata (does not contain versioned properties)
	 */
	public Map<String, Object> getMeta();
	
	/**
	 * @return 0 for folders, size in bytes for files
	 */
	long getFilesize();
	
	/**
	 * Opens a connection to a file and writes its content to a stream.
	 * @param receiver accepts contents of arbitrary length at arbitrary speed
	 * @return access to contents, opened and closed by caller
	 * @throws UnsupportedOperationException if item is folder or backend does not support content reading
	 */
	void getContents(OutputStream receiver) throws UnsupportedOperationException;
	
}
