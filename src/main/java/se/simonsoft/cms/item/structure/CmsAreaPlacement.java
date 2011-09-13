package se.simonsoft.cms.item.structure;

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
	
	public CmsAreaPlacement(String configValue) throws IllegalArgumentException {
		
	}
	
	/**
	 * @return The folder name of the area
	 */
	public String getAreaName() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * TODO what start index is consistent with CmsItemPath, 0 or 1?
	 * @return
	 */
	public int getAreaPathSegmentIndex() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");
	}
	
}
