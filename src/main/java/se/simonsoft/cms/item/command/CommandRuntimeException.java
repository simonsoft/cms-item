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

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommandRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorName = null;
	private String message = null;
	
	private static final int MESSAGE_MAX_LEN = 10000; 
	
	
	public CommandRuntimeException(String errorName) {
		this.errorName = errorName;
	}

	public CommandRuntimeException(String errorName, Throwable cause) {
		super(cause);
		this.errorName = errorName;
		this.setMessage(cause);
	}

	/*
	public CommandRuntimeException(String errorName, String message, Throwable cause) {
		super(message, cause);
		this.errorName = errorName;
	}
	*/

	public String getErrorName() {
		return this.errorName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(Throwable cause) {
		
		StringWriter sw = new StringWriter();
		sw.write("Message: ");
		sw.write(cause.getMessage());
		sw.write("\n");
		
		cause.printStackTrace(new PrintWriter(sw));
		
		if (sw.toString().length() > MESSAGE_MAX_LEN) {
			this.message = sw.toString().substring(0, MESSAGE_MAX_LEN);
		} else {
			this.message = sw.toString();
		}
	}
	

}
