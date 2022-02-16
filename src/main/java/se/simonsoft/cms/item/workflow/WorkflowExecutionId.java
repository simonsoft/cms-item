package se.simonsoft.cms.item.workflow;

import java.util.regex.Pattern;

/**
 * Optional representation of Execution Id providing a UUID if available in the Execution ID.
 * 
 * @author takesson
 *
 */
public class WorkflowExecutionId {

	private final String executionId;
	private final String executionUuid;
	
	private static Pattern uuidPattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
	
	public WorkflowExecutionId(String executionId) {
		
		if (executionId == null || executionId.isBlank()) {
			throw new IllegalArgumentException("executionId must be provided");
		}
		
		this.executionId = executionId;
		this.executionUuid = parseUuid(executionId);
	}

	public boolean hasUuid() {
		return (this.executionUuid != null);
	}
	
	public String getUuid() {
		return this.executionUuid;
	}
	
	@Override
	public String toString() {
		return executionId;
	}
	
	
	
	private static String parseUuid(String executionId) {
		
		String[] executionArn = executionId.split(":");
		// Consider iterating in reverse order until an UUID is found if multiple formats must be supported.
		String executionUuid = executionArn[executionArn.length - 1];
		if (uuidPattern.matcher(executionUuid).matches()) {
			return executionUuid;
		}
		return null;
	}
	
}
