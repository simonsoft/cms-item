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
 */
public interface CmsContentsReader {

	/**
	 * @deprecated the service is per-repository, use {@link #getContents(RepoRevision, CmsItemPath, OutputStream)}
	 */
	void getContents(CmsRepositoryInspection repository, RepoRevision revision, CmsItemPath path, OutputStream out);

	void getContents(RepoRevision revision, CmsItemPath path, OutputStream out);

	/**
	 * @deprecated the service is per-repository, use {@link #getProperties(RepoRevision, CmsItemPath)}
	 */
	CmsItemProperties getProperties(CmsRepositoryInspection repository, RepoRevision revision, CmsItemPath path);
	
	CmsItemProperties getProperties(RepoRevision revision, CmsItemPath path);
	
	/**
	 * Produces raw diff output for an entire changeset.
	 * 
	 * TODO support item diff? support revision range item diff?
	 * Can this be done with svnlook or do we need to parse the full changeset diff?
	 * 
	 * Maybe diff is better suited for inclusion in the Changeset API,
	 * supporting a changeset viewer like the one in Trac.
	 * 
	 * Diff might be a Map&lt;CmsItemPath, Iterable&lt;? extends DiffLine&gt;&gt; with context, added, removed.
	 */
	//void getDiff(CmsRepositoryInspection repository, RepoRevision revision, OutputStream out);
	void getDiff(RepoRevision revision, OutputStream out);
	
	
	CmsItemProperties getRevisionProperties(RepoRevision revision);
	
}
