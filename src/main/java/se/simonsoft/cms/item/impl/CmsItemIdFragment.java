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
package se.simonsoft.cms.item.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;

public class CmsItemIdFragment {
	
	private CmsItemId itemId;
	/**
	 * The fragment is never encoded and should not be considered part of the URL. It should only appear in the XML.
	 * The XML definition of ID and NMTOKEN allows many characters that requires encoding.
	 */
	private String fragment = null;
	
	// Using regex that disallow some chars instead of implementing the XML standard. We can implement the standard later.
	public static String fragmentRegex = "[^#&0-9][^#&]+";
	public static final Pattern FRAGMENT = Pattern.compile("^" + fragmentRegex + "$");
	public static final Pattern NICE = Pattern.compile("^([^#]+)(#" + fragmentRegex + ")?$");
	
	public CmsItemIdFragment(String logicalId) {
		
		if (logicalId == null) {
			throw new IllegalArgumentException("The item id must not be null.");
		}
		if (logicalId.trim().isEmpty()) {
			throw new IllegalArgumentException("The item id must not be empty string.");
		}
		
		Matcher m = NICE.matcher(logicalId);
		if (!m.matches()) {
			throw new IllegalArgumentException("Not a valid item id (fragment allowed): " + logicalId);
		}
		String itemIdStr = m.group(1);
		this.itemId = new CmsItemIdArg(itemIdStr);
		if (m.group(2) != null) {
			this.fragment = m.group(2).substring(1);
		}
		
	}
	
	public CmsItemIdFragment(CmsItemId itemId, String fragment) {
		
		if (fragment != null && !FRAGMENT.matcher(fragment).matches()) {
			throw new IllegalArgumentException("Not a valid fragment id: " + fragment);
		}
		
		this.itemId = itemId;
		this.fragment = fragment;
	}
	
	
	/**
	 * @return the ItemId without fragment
	 */
	public CmsItemId getItemId() {
		
		return this.itemId;
	}
	
	/**
	 * @return the fragment id (without hash character)
	 */
	public String getFragment() {
		
		return fragment;
	}
	
	// TODO: Should we also have withFragment method?
	
	private String appendFragment(String str) {
		
		StringBuilder sb = new StringBuilder(str.length() + 30);
		sb.append(str);
		if (this.fragment != null) {
			sb.append('#');
			sb.append(this.fragment);
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		
		return appendFragment(itemId.toString());
	}
	
	@Override
	public int hashCode() {
		
		return toString().hashCode();
	}
	
	@Override
	public final boolean equals(Object obj) {
		return obj != null
				&& obj instanceof CmsItemIdFragment
				&& equalsId((CmsItemIdFragment) obj);
	}
	
	private final boolean equalsId(CmsItemIdFragment id) {
		
		if (!itemId.equals(id.getItemId())) return false;
		if (getFragment() == null) {
			return id.getFragment() == null;
		} else {
			return getFragment().equals(id.getFragment());
		}
	}
	

	public String getLogicalId() {
		
		return appendFragment(itemId.getLogicalId());
	}

	public String getLogicalIdFull() {
		
		return appendFragment(itemId.getLogicalIdFull());
	}


	public CmsItemIdFragment withRelPath(CmsItemPath newRelPath) {
		
		return new CmsItemIdFragment(this.itemId.withRelPath(newRelPath), this.fragment);
	}


	public CmsItemIdFragment withPegRev(Long newPegRev) {
		
		return new CmsItemIdFragment(this.itemId.withPegRev(newPegRev), this.fragment);
	}


	/** Baseline revision of the CmsItemId regardless if the original ID has a pegRev. 
	 * @param baselineRev
	 * @return itemId with revision no higher than the baselineRev
	 */
	public CmsItemIdFragment withBaselineRev(Long baselineRev) {
		
		if (baselineRev == null) {
			throw new IllegalArgumentException("The baseline revision must not be null.");
		}
		
		if (baselineRev < 1) {
			throw new IllegalArgumentException("The baseline revision must be positive.");
		}
		
		if (itemId.getPegRev() == null || baselineRev < itemId.getPegRev()) {
			return new CmsItemIdFragment(this.itemId.withPegRev(baselineRev), this.fragment);
		}
		
		return this;
	}



}
