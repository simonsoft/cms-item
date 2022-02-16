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
