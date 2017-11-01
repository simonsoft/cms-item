package se.simonsoft.cms.item.command;

public class CommandRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorName = null;
	
	
	public CommandRuntimeException(String errorName) {
		this.errorName = errorName;
	}

	public CommandRuntimeException(String errorName, Throwable cause) {
		super(cause);
		this.errorName = errorName;
	}

	/*
	public CommandRuntimeException(String errorName, String message, Throwable cause) {
		super(message, cause);
		this.errorName = errorName;
	}
	*/

	public String getErrorName() {
		return this.errorName;
	}

}
