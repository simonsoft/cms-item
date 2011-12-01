package se.simonsoft.cms.item.properties;

import java.util.List;
import java.util.Set;

/**
 * Read-only incarnation of {@link SvnPropertyMap}.
 * 
 * The modification and storage behavior of the SvnProperty* classes
 * is still undefined outside adapter code.
 */
public interface CmsItemProperties {

	boolean containsProperty(String key);

	Set<String> getKeySet();
	
	String getString(String key);

	/**
	 * Parses value to a list of strings, check {@link #containsProperty(String)} first.
	 * @param key Property name
	 * @return Property value parsed to list of values, null on parse error or if not set
	 * @throws ClassCastException If a list value is not string (?)
	 */
	List<String> getList(String key) throws ClassCastException;

}