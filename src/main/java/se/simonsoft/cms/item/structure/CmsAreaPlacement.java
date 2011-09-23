package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Represent the configured name and location of known "areas",
 * i.e. parts of the repository with one type of content under it.
 */
public class CmsAreaPlacement {

	/**
	 * Predefined supported translation path setting {@value #LANG_IN_ROOT}.
	 */
	public static final CmsAreaPlacement LANG_IN_ROOT = new CmsAreaPlacement("/lang");
	/**
	 * Predefined supported translation path setting {@value #LANG_IN_PROJ}.
	 */
	public static final CmsAreaPlacement LANG_IN_PROJ = new CmsAreaPlacement("/*/lang");
	
	private String value;
	private int index;
	private String name;
	
	public CmsAreaPlacement(String configValue) throws IllegalArgumentException {
		this.value = configValue;
		read(value);
	}

	private void read(String v) {
		String[] s = v.split("/");
		if (s.length < 2) {
			throw new IllegalArgumentException("Invalid configuration value '" + v + "'");
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
	
	/**
	 * @return The folder name of the area
	 */
	public String getAreaName() {
		return this.name;
	}
	
	/**
	 * Returns the path element index where the area should exist.
	 * Indexes same as in {@link CmsItemPath#getName(int)}
	 * @return 1 for first path segment, 2 for second, ...
	 */
	public int getAreaPathSegmentIndex() {
		return this.index;
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
