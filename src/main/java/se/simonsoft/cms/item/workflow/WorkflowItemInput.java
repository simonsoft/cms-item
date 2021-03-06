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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import se.simonsoft.cms.item.CmsItemId;

public interface WorkflowItemInput {
	
	/**
	 * @return action name as a single token
	 */
	String getAction();
	
	/**
	 * @return item which the action operates on
	 */
	@JsonGetter("itemid")
	CmsItemId getItemId();
	
	/**
	 * @return optional JSON serializable object defining additional action parameters 
	 */
	@JsonInclude(Include.NON_NULL)
	default Object getOptions() {
		return null;
	}
}
