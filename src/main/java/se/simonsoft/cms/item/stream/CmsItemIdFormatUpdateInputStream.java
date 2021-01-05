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
package se.simonsoft.cms.item.stream;

import java.io.InputStream;

import se.simonsoft.cms.item.CmsRepository;

/**
 * InputStream that updates CmsItemId format by removing the ^ character.
 * <ul>
 * <li>Matches only abbreviated CmsItemId because qualified CmsItemIds (with hostname) should never be stored in data.</li>
 * <li>Matches only attribute content by requiring a leading " (quote) character.</li>
 * <li>Repository CmsItemId will have an incorrect remaining trailing slash.</li>
 * </ul>
 */
public class CmsItemIdFormatUpdateInputStream extends ReplacingInputStream {

	public CmsItemIdFormatUpdateInputStream(InputStream in, CmsRepository cmsRepo) {
		
		super(in, "\"" + cmsRepo.getItemId(null, null).getLogicalId() + "^", "\"" + cmsRepo.getItemId(null, null).getLogicalId());
	}

}
