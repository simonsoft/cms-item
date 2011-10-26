package se.simonsoft.cms.item.properties;

import java.util.Arrays;
import java.util.List;


public class SvnPropertyValueString implements SvnPropertyValue<String> {
	
	private String value;
	private boolean modified;
	
	/**
	 * RegEx pattern used to split on newline, Subversion convention.
	 * Note that the value should be trimmed before splitting.
	 */
	public static final String NEWLINE_SPLIT = "\\s*\\n\\s*";

	SvnPropertyValueString(String value, boolean modified) {
	
		this.value = value;
		this.modified = modified;
	}
	
	public SvnPropertyValueString(String value) {
		
		this(value, true);
	}

	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#getValue()
	 */
	@Override
	public String getValue() {

		return value;
	}

	/**
	 * Splits a value using the Subversion convention with newline separation,
	 * used for example in svn:ignore and svn:externals.
	 * 
	 * @return Trimmed, non-empty lines from the property value,
	 *  null for no such property, empty list for existing property without data,
	 *  trimmed single entry list for no newlines in value,
	 *  immutable.
	 */
	public List<String> getValueNewlineSeparated() {
		String v = getValue();
		if (v == null) {
			return null;
		}
		v = v.trim();
		if (v.length() == 0) {
			return Arrays.asList(new String[] {});
		}
		return Arrays.asList(v.split(NEWLINE_SPLIT));
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
	SvnPropertyValue<String> setString(String value) {
		
		SvnPropertyValueString result;
		
		if (this.value == null || !this.value.equals(value)) {
			result = new SvnPropertyValueString(value, true);
		} else {
			result = this;
		}
		return result;
	}

	
	/*
	void setValue(SvnPropertyValue<String> b) {
		
		// Currently very simple since we can only store a String.
		setString(b.getValue());
	}
	*/
	
	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#getSvnString()
	 */
	@Override
	public String getSvnString() {
		
		return this.toString();
	}
	
	public String toString() {
		
		return value;
	}
	
	public boolean equals(Object y) {

		if (y == null) {
			return false;
		}
		return this.toString().equals(y.toString());

	}
	
	public int hashCode() {
		
		return this.toString().hashCode();
	}

	
	
	

}
