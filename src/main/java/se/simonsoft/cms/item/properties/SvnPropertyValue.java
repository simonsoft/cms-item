package se.simonsoft.cms.item.properties;

/**
 * Holder of property value.
 * @param <T> The java abstraction of the stored value
 */
public interface SvnPropertyValue<T> {

	/**
	 * @return true if the value has been modified locally
	 */
	public abstract boolean isModified();	
	
	/**
	 * Returns immutable (or cloned) current value.
	 * If the returned value is mutable it can be changed freely
	 * and changes will not affect the instance's value.
	 * 
	 * @return the parsed property value mapped to a Java type
	 */
	public abstract T getValue();

	/** 
	 * Provides the String representation used when persisting as a Subversion property.
	 * @return The persistence string representation.
	 */
	public abstract String getSvnString();
	
	/**
	 * @return true if same value type AND the written value when committed would be equal
	 */
	public abstract boolean equals(Object obj);

}