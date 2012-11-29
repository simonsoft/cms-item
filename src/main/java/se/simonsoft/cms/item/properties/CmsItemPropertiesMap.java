package se.simonsoft.cms.item.properties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Subset of property functionality with creation API geared towards
 * temporary instances for example to define property changes in commit.
 */
public class CmsItemPropertiesMap 
		extends LinkedHashMap<String, Object> 
		implements Map<String, Object>, CmsItemProperties {

	private static final long serialVersionUID = 1L;

	public CmsItemPropertiesMap() {
		super();
	}
	
	public CmsItemPropertiesMap(String key, String value) {
		this();
		this.put(key, value);
	}
	
	public CmsItemPropertiesMap and(String key, String value) {
		andCheck(key);
		this.put(key, value);
		return this;
	}
	
	public CmsItemPropertiesMap andDelete(String key) {
		andCheck(key);
		this.put(key, null);
		return this;
	}
	
	void andCheck(String key) {
		if (containsKey(key)) {
			String msg = "Property '" + key + "' is already" + (get(key) == null ? " deleted" : " set");
			throw new IllegalArgumentException(msg);
		}
	}
	
	@Override
	public boolean containsProperty(String key) {
		return containsKey(key);
	}

	@Override
	public Set<String> getKeySet() {
		return keySet();
	}

	@Override
	public List<String> getList(String key) throws ClassCastException {
		throw new IllegalArgumentException("not implemented");
	}	
	
	@Override
	public String getString(String key) {
		// for now we guard against inserting anything but strings
		return (String) get(key);
	}

	@Override
	public Object put(String key, Object value) {
		if (value == null || value instanceof String) {
			return super.put(key, value);
		}
		throw new IllegalArgumentException("Property of type " + value.getClass().getName() + " not supported");
	}

}
