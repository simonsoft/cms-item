package se.simonsoft.cms.item.workflow;

import java.util.Date;
import java.util.Set;

import se.simonsoft.cms.item.CmsItemId;

public interface WorkflowExecutionStatus {
	
	Set<WorkflowExecution> getWorkflowExecutions(CmsItemId itemId, boolean refresh);
	
	void onStartRunning(String id, Date startDate, WorkflowItemInput input);
	
	void onStartFailed(Date startDate, WorkflowItemInput input);
	
}
