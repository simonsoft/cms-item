/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true) // Relaxed in this base class in order to parse when only interested in this limited information. 
@JsonInclude(Include.NON_NULL)
public class WorkflowConfig {

	protected String label;
	protected String description;
	//protected String pathsegment0; // TODO: Consider filter for project-folder when defining config in Parameter store (due to secrets in config).

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
