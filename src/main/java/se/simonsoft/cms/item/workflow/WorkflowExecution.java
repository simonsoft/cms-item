package se.simonsoft.cms.item.workflow;

import java.util.Date;

public final class WorkflowExecution {

	private final String id;
	private final String status;
	private final Date startDate;
	private final Date stopDate;
	private final WorkflowItemInput itemInput;
	
	
	public WorkflowExecution(
				String id,
				String status,
				Date startDate,
				Date stopDate,
				WorkflowItemInput itemInput
					) {
		
		this.id = id;
		this.status = status;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.itemInput = itemInput;
	}

	public String getId() {
		return id;
	}


	public String getStatus() {
		return status;
	}


	public Date getStartDate() {
		return startDate;
	}


	public Date getStopDate() {
		return stopDate;
	}


	public WorkflowItemInput getItemInput() {
		return itemInput;
	}
}
