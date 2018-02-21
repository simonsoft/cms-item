package se.simonsoft.cms.item.workflow;

public class WorkflowExecutionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Will preserve Throwables message.
	 * @param cause
	 */
	public WorkflowExecutionException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
	
	public WorkflowExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
}
