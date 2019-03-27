package se.simonsoft.cms.item;

/**
 * A runtime exception that explicitly carries a message to the end user.
 * This exception should typically not be wrapped by other, less specific, exceptions.
 * 
 * This exception class might be useful for localization in the future.
 */
public class MessageRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public MessageRuntimeException(String message) {
		super(message);
	}

	public MessageRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}


}
