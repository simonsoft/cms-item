package se.simonsoft.cms.item.properties;


public class SvnPropertyValueString implements SvnPropertyValue<String> {
	
	private String value;
	private boolean modified;

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
