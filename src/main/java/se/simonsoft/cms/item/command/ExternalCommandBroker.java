/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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

import se.simonsoft.cms.item.CmsItemId;

/**
 * Concept for registering known actions at compile time,
 * together with the expected arguments.
 * 
 * The goal is to abstract away from the command execution
 * how invocation and arguments were transfered.
 * At the same time we get a list of known actions,
 * and cleanly separated handling functions.
 */
public interface ExternalCommandBroker {

	/**
	 * Registers an action with a handler and optional arguments wrapper class.
	 * 
	 * Note that for services to handle multiple actions, handlers must be registered
	 * using anonymous inner classes with calls to different functions in the service,
	 * because argumentsClass type alone is not enough to identify the
	 * {@link ExternalCommandHandler#handleExternalCommand(CmsItemId, Object)} method to call.
	 * 
	 * @param action The action name to register
	 * @param handler The handler that should take care of all commands for this action
	 * @param argumentsClass The class that has fields corresponding to optional action arguments,
	 *   null if no custom arguments should be supported
	 */
	<T> void registerHandler(String action, ExternalCommandHandler<T> handler, Class<T> argumentsClass);
	
	/**
	 * @param action
	 * @param handler
	 * @deprecated Replaced with ExternalCommandHandler<Void>.
	 */
	void registerHandler(String action, ExternalCommandHandlerNoArgs handler);
	
	/**
	 * @param action an action name
	 * @return true if the action has been registered
	 */
	boolean supportsAction(String action);
	
	/**
	 * @return true if an action expects addtional arguments (jsonargs)
	 */
	boolean expectsArguments(String action);
	
	
	Class<?> getArgumentsClass(String action);
	
	/**
	 * Translates external invocations to events.
	 * @param command The invocation parameters
	 * @throws UnknownActionException if the action name has not been registered
	 * @throws ArgsValidationException if the handler is noargs but jsonargs is not null or vice versa,
	 *  or if jsonargs is set but creation of the arguments wrapper object failed.
	 * @throws RuntimeException if the handler runs into an unrecoverable exceptions
	 */
	<T> void execute(ExternalCommand<T> command) 
			throws UnknownActionException, ArgsValidationException;
	
}
