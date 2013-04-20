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

import se.simonsoft.cms.item.Checksum;
import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;

/**
 * Optional base class for {@link CmsItem}, throwing {@link UnsupportedOperationException} in all methods.
 * 
 * Extended by implementations that don't want compilation errors as new methods are added.
 * Also useful for anonymous inner classes in unit tests.
 */
public abstract class CmsItemBase implements CmsItem {

	@Override
	public CmsItemId getId() {
		throw new UnsupportedOperationException("getId not implemented");
	}

	@Override
	public String getStatus() {
		throw new UnsupportedOperationException("getStatus not implemented");
	}

	@Override
	public Checksum getChecksum() {
		throw new UnsupportedOperationException("getChecksum not implemented");
	}

}
