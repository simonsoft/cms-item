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
package se.simonsoft.cms.item.command;

/**
 * Context of an external command execution, provided by the caller of the broker.
 */
public class ExternalCommandContext {

	/**
	 * Error name of a {@link CommandRuntimeException} telling the workflow worker to leave the task open,
	 * completed later by a callback using the task token (or timeout).
	 */
	public static final String TASK_DEFERRED = "TaskDeferred";
	
	private final String taskToken;
	
	
	public ExternalCommandContext(String taskToken) {
		this.taskToken = taskToken;
	}
	
	/**
	 * @return task token of the workflow task executing the command, a secret, null if not executed as a workflow task
	 */
	public String getTaskToken() {
		return taskToken;
	}
}
