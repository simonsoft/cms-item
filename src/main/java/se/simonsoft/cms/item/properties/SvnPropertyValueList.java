package se.simonsoft.cms.item.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * List stored as a JSON array, currently for string but could easily be 
 * generic if the stored type has a toString.
 * <p>
 * Modification / dirty state detection was inherited from the string
 * implementation. It is quite tricky for lists, but the current solution is simple:
 * The contained value is immutable.
 * This means that the method {@link #isModified()}
 * just keeps the state from the constructor (useful in SvnPropertyMap) but
 * {@link #equals(Object)} becomes very important for {@link SvnPropertyMap#putProperty(String, SvnPropertyValue)}.
 */
public class SvnPropertyValueList implements SvnPropertyValue<List<String>> {
	
	private List<?> value;
	private boolean modified;
	
	private static JSONParser parser;
	
	static {
		// configure reusable parser
		parser = new JSONParser();
	}

	/**
	 * Package scoped because it allows the modified state to be changed.
	 * The instance copies the <em>current contents</em> of the list and does not keep a reference to it.
	 * @param value The property value
	 * @param modified set to false to state that the value is identical to the current stored value
	 */
	SvnPropertyValueList(List<?> value, boolean modified) {
		this.value = copy(value);
		this.modified = modified;
	}
	
	private <T> List<T> copy(List<T> source) {
		return new ArrayList<T>(source);
	}
	
	/**
	 * Default constructor that initializes a new value and sets state to modified.
	 * @param value The property value as java abstraction
	 */
	public SvnPropertyValueList(List<?> value) {
		
		this(value, true);
	}

	/**
	 * Attempts to parse property value and throws exception if
	 * the value does not follow the rules of this value class.
	 * Used only internally for parsing so {@link #modified} is set to <code>false</code>.
	 * @param svnPropertyValue The raw string from Subversion
	 * @throws ValueParseException If the string is not parseable to this class's data structure
	 */
	protected SvnPropertyValueList(String svnPropertyValue) throws ValueParseException {
		this(parse(svnPropertyValue), false);
	}
	
	private static List<?> parse(String svnPropertyValue) throws ValueParseException {
		Object obj;
		try {
			obj = parser.parse(svnPropertyValue);
		} catch (ParseException e) {
			throw new ValueParseException(e, svnPropertyValue);
		}
		JSONArray array= (JSONArray) obj;
		try {
			return (List<?>) array;
		} catch (ClassCastException e) {
			// This hasn't happened so far. Probably the generic type can not be checked at runtime.
			throw new RuntimeException("Failed to handle array objects as strings.", e);
		}
	}

	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#getValue()
	 */
	@Override
	public List<String> getValue() {
		ArrayList<String> v = new ArrayList<String>(value.size());
		for (Object x : value) {
			v.add(jsonElementToString(x));
		}
		return v;
	}

	/**
	 * Converts from objects produced by the json library to strings, regardless of JSON type
	 * @param x Parsed JSON value or null
	 * @return Serialized or stringified value
	 */
	protected String jsonElementToString(Object x) {
		if (x == null) {
			return null;
		}
		return x.toString();
	}
	
	/**
	 * Makes sure all items in the list are either maps or null.
	 * @return property value
	 * @throws ValueParseException if any item was it parseable to map with string key and string value
	 */
	public List<Map<String,String>> getValueMaps() throws ValueParseException {
		ArrayList<Map<String,String>> v = new ArrayList<Map<String,String>>(value.size());
		for (int i = 0; i < value.size(); i++) {
			Object x = value.get(i);
			if (x == null) {
				v.add(null);
			} else {
				if (x instanceof Map) {
					@SuppressWarnings("unchecked") Map<String,Object> xm = (Map<String,Object>) x;
					Map<String,String> m = new HashMap<String,String>();
					// keys in JSON are always strings
					for (Map.Entry<String, Object> e : xm.entrySet()) {
						m.put(e.getKey(), jsonElementToString(e.getValue()));
					}
					v.add(m);
				} else {
					throw new ValueParseException("Not a JSON map at index " + i);
				}
			}
		}		
		return v;
	}


	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#isModified()
	 */
	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * Package scoped because the rest of the application should go through {@link SvnPropertyMap}.
	 * @param value the new value
	 */
	SvnPropertyValue<List<String>> setList(List<?> value) {
		
		SvnPropertyValue<List<String>> result;
		
		if (this.value == null || !this.value.equals(value)) {
			result = new SvnPropertyValueList(value, true);
		} else {
			result = this;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#getSvnString()
	 */
	@Override
	public String getSvnString() {
		return JSONValue.toJSONString(this.value);
	}
	
	public String toString() {
		
		return value.toString();
	}

	public static List<?> set(List<?> values, int index, Object element) {
		
		// toArray happens to solve exactly this use case by:
		// - accepting a too large array 
		// - otherwise allocate the exact
		Object[] array = values.toArray(new Object[index + 1]);
		array[index] = element;
		int newLength = array.length;
		while (array[newLength - 1] == null) {
			newLength--;
		}
		return Arrays.asList(Arrays.copyOf(array, newLength));
	}
	
	/**
	 * @return value for equals and hashCode
	 */
	protected String getComparisonString() {
		// Is this too slow? Do we do a lot of list operations? Should we serialise when value is set?
		return this.getSvnString();
	}
	
	@Override
	public int hashCode() {
		return this.getComparisonString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SvnPropertyValueList) {
			return this.getComparisonString().equals(
					((SvnPropertyValueList) obj).getComparisonString());
		}
		return false;
	}

}
