/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.simonsoft.cms.item.CmsItemId;

public class ExternalCommand<T> {

	protected CmsItemId itemId;
	protected String action;
	protected T args = null;
	
	
	private static final Logger logger = LoggerFactory.getLogger(ExternalCommand.class);
	
	
	public ExternalCommand(CmsItemId itemId, String action) {
		this.itemId = itemId;
		this.action = action;
	}
	
	public ExternalCommand(CmsItemId itemId, String action, T args) {
		this.itemId = itemId;
		this.action = action;
		this.args = args;
	}
	
	
	public String getAction() {
		return action;
	}
	
	public CmsItemId getItemId() {
		return itemId;
	}

	
	public boolean hasArgs() {
		return args != null;
	}
	
	
	// The Class<?> parameter is intended for subclasses that actually do parsing in this method.
	public T getArgs(Class<?> argumentsClass) throws ArgsValidationException {
		if (args == null && argumentsClass == Void.class) {
			return null;
		}
		
		if (args == null) {
			logger.error("Requested arguments when command does not have arguments.");
			throw new ArgsValidationException("Requested arguments when command does not have arguments.");
		}
		
		if (!argumentsClass.isInstance(args)) {
			logger.error("Requested arguments class does not match. {}", args.getClass());
			throw new ArgsValidationException("Requested arguments class does not match: " + args.getClass());
		}
		return args;
	}
}
