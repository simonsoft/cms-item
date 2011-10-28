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

	List<String> getList(String key) throws ClassCastException;

}