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

import java.time.Instant;

public final class WorkflowExecution {

	private final String id;
	private final String status;
	private final Instant startDate;
	private final Instant stopDate;
	private final WorkflowItemInput input;
	
	
	public WorkflowExecution(
				String id,
				String status,
				Instant startDate,
				Instant stopDate,
				WorkflowItemInput input
					) {
		
		this.id = id;
		this.status = status;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.input = input;
	}

	public String getId() {
		return id;
	}


	public String getStatus() {
		return status;
	}


	public Instant getStartDate() {
		return startDate;
	}


	public Instant getStopDate() {
		return stopDate;
	}


	public WorkflowItemInput getInput() {
		return input;
	}
}
