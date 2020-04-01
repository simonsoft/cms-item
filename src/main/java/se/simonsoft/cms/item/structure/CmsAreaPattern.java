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
package se.simonsoft.cms.item.structure;

import java.util.LinkedList;
import java.util.List;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Understands the CMS configuration strings used to define sections of the
 * repository such as translations and releases, provides path transformations.
 * <p>
 * Sample config strings: <code>/lang</code>, <code>/*</code><code>/lang</code>, <code>lang</code>.  
 * <p>
 * This is a low level class designed to understands a specific CMS config syntax.
 * It is highly recommended for calling code to add an abstraction layer that
 * separates configuration and path transformation, for example a translation
 * service with area patterns injected that has getTranslationPath(master).
 * <p>
 * If using this API directly from business logic consider marking your code's intent
 * by instead calling {@link CmsTranslationsPattern} or {@link CmsReleasesPattern}.
 * <p>
 * An higher layer abstraction could use CmsItemId as input, to allow for example
 * revision numbers taken into account when transforming paths.
 */
public class CmsAreaPattern {
	
	private String value;
	private int index;
	private String name;
	
	public CmsAreaPattern(String configValue) throws IllegalArgumentException {
		this.value = configValue;
		read(value);
	}
	
	/**
	 * Returns the top level folder of releases or translations, parent of label folders.
	 * @param master A path outside area, could be a folder as long as path depth is larger than {@link #getAreaItemPathOffset()}
	 * @return The path to the area folder inside the corresponding slave would be placed 
	 */
	public CmsItemPath getArea(CmsItemPath master) {
		List<String> a = new LinkedList<String>(master.getPathSegments());
		if (isAreaRelative()) {
			a.remove(a.size() - 1);
		} else {
			a = a.subList(0, getAreaPathSegmentIndex() - 1);
		}
		a.add(getAreaName());
		return concat(a);
	}
	
	/**
	 * Allows a layer of abstraction that could be used to reduce folder depth inside area.
	 * @return The path element to start at when inserting item in area,
	 *  0 for appending the full original path after {@link #getArea(CmsItemPath)} and label.
	 */
	public int getAreaItemPathOffset() {
		return getAreaPathSegmentIndex() - 1;
	}
	
	/**
	 * @return True if the path is inside the area and has a label.
	 */
	public boolean isPathInside(CmsItemPath path) {
		int ai = getAreaPathSegmentIndex();
		if (isAreaRelative()) {
			return -ai + 1 < path.getPathSegments().size()
				&& path.getName(ai - 2).equals(getAreaName());
		} else {
			return ai + 1 < path.getPathSegments().size() // must have one additional segment after area
				&& path.getName(ai).equals(getAreaName());
		}
	}
	
	public boolean isPathOutside(CmsItemPath path) {
		return !isPathInside(path);
	}
	
	/**
	 * Transforms a master path to corresponding slave given a label.
	 * Slave could be a translation and label would then be a language code.
	 */
	public CmsItemPath getPathInside(CmsItemPath master, String destinationLabel) {
		if (isPathInside(master)) {
			if (getPathLabel(master).equals(destinationLabel)) {
				throw new IllegalArgumentException("Path already has label '" + destinationLabel + "': " + master);
			}
			master = getPathOutside(master);
		}
		List<String> p = new LinkedList<String>(master.getPathSegments());
		int insertionPoint = getAreaPathSegmentIndex() - 1;
		if (isAreaRelative()) { 
			insertionPoint = p.size() + getAreaPathSegmentIndex();
		}
		p.add(insertionPoint, destinationLabel);
		p.add(insertionPoint, getAreaName());
		return concat(p);
	}
	
	/**
	 * Extracts the label from a path inside the area,
	 * label could be a translation locale or a release label.
	 * @param pathInsideArea
	 * @return label according to path
	 * @throws IllegalArgumentException if the path is not inside area
	 */
	public String getPathLabel(CmsItemPath pathInsideArea) {
		if (!isPathInside(pathInsideArea)) {
			throw new IllegalArgumentException("Path is a not inside area " + toString() + ":" + pathInsideArea);
		}
		int labelPoint = getAreaPathSegmentIndex() + 1;
		if (isAreaRelative()) {
			labelPoint = labelPoint - 2;
		}
		return pathInsideArea.getName(labelPoint);
	}
	
	/**
	 * @return master path
	 */
	public CmsItemPath getPathOutside(CmsItemPath pathInsideArea) {
		List<String> p = new LinkedList<String>(pathInsideArea.getPathSegments());
		int deletionPoint = getAreaPathSegmentIndex() - 1;
		if (isAreaRelative()) {
			deletionPoint = p.size() + getAreaPathSegmentIndex() - 2;
		}
		p.remove(deletionPoint + 1); // remove label
		p.remove(deletionPoint);
		return concat(p);
	}
	
	protected CmsItemPath concat(List<String> subPaths) {
		StringBuffer b = new StringBuffer();
		for (String s : subPaths) {
			b.append('/').append(s);
		}
		return new CmsItemPath(b.toString());
	}
	
	/**
	 * @return The folder name of the area
	 */
	String getAreaName() {
		return this.name;
	}
	
	/**
	 * Returns the path element index where the area should exist.
	 * Indexes same as in {@link CmsItemPath#getName(int)}
	 * @return 1 for first path segment, 2 for second, ...
	 */
	int getAreaPathSegmentIndex() {
		return this.index;
	}
	
	boolean isAreaRelative() {
		return getAreaPathSegmentIndex() < 0;
	}

	/**
	 * Parses config value to instance fields.
	 * @param v from constructor
	 */
	protected void read(String v) {
		String[] s = v.split("/");
		if (s.length < 2) {
			if (v.startsWith("/")) {
				throw new IllegalArgumentException("Invalid configuration value '" + v + "'");
			}
			this.index = -1;
			this.name = s[0];
			return;
		}
		if (s.length == 2) {
			this.index = 1;
			this.name = s[1];
			return;
		}
		if (s.length > 3) {
			throw new IllegalArgumentException("Invalid configuration value '" + v + "'. Depth can be no more than 2.");
		}
		if (s[1].equals("*")) {
			this.index = 2;
			this.name = s[2];
			return;
		} else {
			throw new IllegalArgumentException("Invalid configuration value '" + v + "'. Must mark level.");
		}
	}
	
	@Override
	public String toString() {
		return this.value;
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals("" + obj);
	}	
	
}
