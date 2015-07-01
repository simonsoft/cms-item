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
package se.simonsoft.cms.item.events.change;

import java.util.Comparator;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Compare two CmsChangesetItem based on their CmsItemPath.
 * Repository root (null) sorts at the top.
 *
 */
public class CmsChangesetItemComparator implements Comparator<CmsChangesetItem> {


	@Override
	public int compare(CmsChangesetItem o1, CmsChangesetItem o2) {
		
		CmsItemPath p1 = o1.getPath();
		CmsItemPath p2 = o2.getPath();
		
		if (p1 == null && p2 == null) {
			return 0;
		}
		if (p1 == null) {
			return -1;
		}
		if (p2 == null) {
			return 1;
		}
		return p1.compareTo(p2);
	}

}
