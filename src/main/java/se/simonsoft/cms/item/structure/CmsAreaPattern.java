package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Understands the CMS configuration strings used to define sections of the
 * repository such as translations and releases, provides path transformations.
 * <p>
 * Sample config strings: <code>{@value #LANG_IN_ROOT}</code>, <code>{@value #LANG_IN_PROJ}</code>.  
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

	/**
	 * Sample translation path setting {@value #LANG_IN_ROOT}.
	 */
	public static final CmsAreaPattern LANG_IN_ROOT = new CmsAreaPattern("/lang");
	/**
	 * Sample translation path setting {@value #LANG_IN_PROJ}.
	 */
	public static final CmsAreaPattern LANG_IN_PROJ = new CmsAreaPattern("/*/lang");
	
	private String value;
	private int index;
	private String name;
	
	public CmsAreaPattern(String configValue) throws IllegalArgumentException {
		this.value = configValue;
		read(value);
	}
	
	/**
	 * @return True if the path is inside the area and has a label.
	 */
	public boolean isPathInside(CmsItemPath path) {
		int ai = getAreaPathSegmentIndex();
		return ai + 1 < path.getPathSegments().size() // must have one additional segment after area
				&& path.getName(ai).equals(getAreaName());
	}
	
	/**
	 * Transforms a master path to corresponding slave given a label.
	 * Slave could be a translation and label would then be a language code.
	 */
	public CmsItemPath getPathInside(CmsItemPath master, String destinationLabel) {
		StringBuffer b = new StringBuffer();
		int n = getAreaPathSegmentIndex();
		int i = 0;
		for (String s : master.getPathSegments()) {
			if (++i == n) {
				b.append('/').append(getAreaName()).append('/').append(destinationLabel);
			}
			b.append('/').append(s);
		}
		return new CmsItemPath(b.toString());
	}
	
	/**
	 * Extracts the label from a path inside the area,
	 * label could be a language code or a release name.
	 * @param pathInsideArea
	 * @return language code according to path
	 * @throws IllegalArgumentException if the path not {@link #isTranslation(CmsItemPath)}
	 */
	public String getPathLabel(CmsItemPath pathInsideArea) {
		if (!isPathInside(pathInsideArea)) {
			throw new IllegalArgumentException("Path is a not inside area " + toString() + ":" + pathInsideArea);
		}
		return pathInsideArea.getName(getAreaPathSegmentIndex() + 1);
	}
	
	public CmsItemPath getPathOutside(CmsItemPath pathInsideArea) {
		throw new UnsupportedOperationException("not implemented");
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

	/**
	 * Parses config value to instance fields.
	 * @param v from constructor
	 */
	protected void read(String v) {
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
	
	@Override
	public String toString() {
		return this.value;
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals("" + obj);
	}	
	
}
