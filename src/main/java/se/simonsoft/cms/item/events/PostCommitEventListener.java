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
package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Gets notified when a commit is done to the CMS.
 * 
 * Only privileged services can use this to access the repository,
 * because there is no authentication available.
 * 
 * Any service might be interested in the event, for caching etc,
 * because between these events nothing changes in the repositories
 * except locks and exceptionally revprops.
 * 
 * Listeners are registered by binding to a set of this interface
 * using the multibinder feature.
 * Hooks are executed in the order they are bound.
 */
public interface PostCommitEventListener {

	public void onPostCommit(CmsRepository repository, RepoRevision revision);
	
}
