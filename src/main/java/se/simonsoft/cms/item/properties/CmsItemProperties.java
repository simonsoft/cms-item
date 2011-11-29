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
	 * @param key Property name
	 * @return Property value parsed to list of values, null if property is not set,
	 *  TODO properly define behavior if value is not a list, return null, ClassCastException or ValueParseException?
	 * @throws ClassCastException legacy?
	 */
	List<String> getList(String key) throws ClassCastException;

}