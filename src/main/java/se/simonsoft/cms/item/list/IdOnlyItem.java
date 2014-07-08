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
package se.simonsoft.cms.item.list;

import java.io.OutputStream;
import java.util.Map;

import se.simonsoft.cms.item.Checksum;
import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemKind;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * To use {@link CmsItemList} when the only information available is {@link CmsItemId}'s.
 */
public class IdOnlyItem implements CmsItem {

	private CmsItemId id;

	public IdOnlyItem(CmsItemId id) {
		this.id = id;
	}
	
	@Override
	public CmsItemId getId() {
		return id;
	}

	@Override
	public RepoRevision getRevisionChanged() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}

	@Override
	public String getRevisionChangedAuthor() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}
	
	@Override
	public CmsItemKind getKind() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}

	@Override
	public String getStatus() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}

	@Override
	public Checksum getChecksum() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}

	@Override
	public CmsItemProperties getProperties() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}
	
	@Override
	public Map<String, Object> getMeta() {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}

	@Override
	public long getFilesize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void getContents(OutputStream receiver)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Only the ID is available for " + id);
	}

}
