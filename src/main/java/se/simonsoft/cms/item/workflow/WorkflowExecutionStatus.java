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
import java.util.Set;

import se.simonsoft.cms.item.CmsItemId;

public interface WorkflowExecutionStatus {
	
	WorkflowExecution getWorkflowExecution(String id);
	
	Set<WorkflowExecution> getWorkflowExecutions(CmsItemId itemId);

	@Deprecated
	Set<WorkflowExecution> getWorkflowExecutions(CmsItemId itemId, boolean refresh);
	
	void onStartRunning(String id, Instant startDate, WorkflowItemInput input);
	
	void onStartFailed(Instant startDate, WorkflowItemInput input);
	
}
