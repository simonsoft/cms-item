package se.simonsoft.cms.item.config;

/**
 * Represents a single configuration option.
 * By wrapping the value into a class we can extend the features in the future,
 * e.g. providing access to all inherited values (for "merging" values)
 * @author takesson
 *
 */
public interface CmsConfigOption {

	
	/**
	 * @return the name/key for the config option
	 */
	public String getName();
	
	/**
	 * @return the value of the config option as String, where inherited properties are resolved by override.
	 */
	public String getValueString();
	
	/**
	 * @return the value of the config option as Boolean, where case-insensitive and trimmed "true" becomes True. 
	 */
	public Boolean getValueBoolean();
}
