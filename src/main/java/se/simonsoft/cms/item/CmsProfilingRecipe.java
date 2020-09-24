package se.simonsoft.cms.item;

/** 
 * Contains the name and conditions for a single profiling configuration. 
 * Typically corresponds to one profiles element in an XML document.
 * The conditions are defined by each implementation of this interface.
 */
public interface CmsProfilingRecipe {

	public String getName();
}
