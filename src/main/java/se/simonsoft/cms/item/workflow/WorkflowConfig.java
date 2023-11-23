package se.simonsoft.cms.item.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true) // Relaxed in this base class in order to parse when only interested in this limited information. 
@JsonInclude(Include.NON_NULL)
public class WorkflowConfig {

	protected String label;
	protected String description;

	// TODO: Consider a structure that could suggest / define values for other UI input, e.g. each control can get 'value' and/or 'disabled'. 

	public WorkflowConfig() {
	}

	/**
	 * @return label intended for the user
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Description is available on the config object and suppressed when constructing a workflow execution.
	 * 
	 * @return description intended for the user (null in workflow execution)
	 */
	public String getDescription() {
		return this.description;
	}

}
