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
package se.simonsoft.cms.item;

/**
 * A runtime exception that explicitly carries a message to the end user.
 * This exception should typically not be wrapped by other, less specific, exceptions.
 * 
 * This exception class might be useful for localization in the future.
 */
public class MessageRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;


	public MessageRuntimeException(String message) {
		super(message);
	}

	public MessageRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}


}
