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
package se.simonsoft.cms.item.inspection;

import java.io.OutputStream;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.events.change.CmsChangeset;
import se.simonsoft.cms.item.events.change.CmsChangesetItem;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Provides privileged and fast access to item contents.
 * 
 * Using a separate service instead of access through {@link CmsChangeset}
 * because there is no relation to the current commit.
 * 
 * In particular we must support reading of {@link CmsChangesetItem}
 * followed by the item from {@link CmsChangesetItem#getPreviousChange()}.
 * 
 * TODO: #919 Consider deprecating since CmsItemLookup can provide contents and props.
 * TODO: #919 Alternatively to avoid refactoring in repos-indexing; build CmsContentsReaderShim using CmsItemLookup and a caffeine cache of items (no content).
 * TODO: #919 Consider implementing getRevisionProperties in CmsRepositoryLookup for http.
 */
public interface CmsContentsReader {


	void getContents(RepoRevision revision, CmsItemPath path, OutputStream out);

	
	CmsItemProperties getProperties(RepoRevision revision, CmsItemPath path);
	
	
	CmsItemProperties getRevisionProperties(RepoRevision revision);
	
}
