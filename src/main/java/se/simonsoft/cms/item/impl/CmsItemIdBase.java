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
package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.CmsItemId;

/**
 * Same equals and hashCode for different impls, important for collections etc.
 * 
 * Not allowed to by itself encode URLs for {@link #getUrl()} and {@link #getUrlAtHost()}.
 */
public abstract class CmsItemIdBase implements CmsItemId {

	@Override
	public final boolean equals(Object obj) {
		return obj != null
				&& obj instanceof CmsItemId
				&& equalsId((CmsItemId) obj);
	}

	private final boolean equalsId(CmsItemId id) {
		if (!getRepository().equals(id.getRepository())) return false;
		if (getRelPath() == null) {
			if (id.getRelPath() != null) return false;
		} else if (!getRelPath().equals(id.getRelPath())) return false;
		if (getPegRev() == null) {
			return id.getPegRev() == null;
		} else {
			return getPegRev().equals(id.getPegRev());
		}
	}

	@Override
	public int hashCode() {
		return (getRepository().getPath() + getRelPath() + getPegRev()).hashCode();
	}

}