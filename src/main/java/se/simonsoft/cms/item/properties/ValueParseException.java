package se.simonsoft.cms.item.properties;

public class ValueParseException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates consistent error message based on exception and original value.
	 * @param cause The parse error
	 * @param value The attempted value
	 */
	public ValueParseException(Throwable cause, String value) {
		super("Failed to parse property value: " + value, cause);
	}

	public ValueParseException(String msg) {
		super(msg);
	}

}
