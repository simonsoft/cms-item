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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class CommandRuntimeExceptionTest {
	
	private static final int MESSAGE_MAX_LEN = 10000; 

	@SuppressWarnings("null")
	@Test
	public void testMessageShortStacktrace() throws Exception {
		
		String npeThrow = null;
		try {
			npeThrow.concat("test");
		} catch (NullPointerException e) {
			CommandRuntimeException cre = new CommandRuntimeException("ConcatOnNull", e);
			assertEquals("ConcatOnNull", cre.getErrorName());
			String message = cre.getMessage();
			System.out.println(message);
			assertTrue("Message contains underlying exceptions message", message.contains("Message: null"));
			assertTrue("NPE should be inluded in message", message.contains("java.lang.NullPointerException"));
			assertTrue("This class name should be included", message.contains("CommandRuntimeExceptionTest.java"));
			//Hard to get a stacktrace that exceeds max length.
			assertTrue("Longer messages then 10000 is substringed", message.getBytes().length <= MESSAGE_MAX_LEN);
		}
	}
	
	@Test
	public void testContainsUnderlygingExceptionsMessage() throws Exception {
		try {
			throw new IllegalArgumentException("Underlying exceptions message");
		} catch (IllegalArgumentException e) {
			CommandRuntimeException cre = new CommandRuntimeException("IAE", e);
			String message = cre.getMessage();
			assertTrue("Contains underlying exceptions message", message.contains("Message: Underlying exceptions message"));
		}
		
	}
	
	
	@Test
	public void testUnderlyingExceptionMessageIsNull() {
		try {
			throw new IllegalArgumentException();
		} catch (IllegalArgumentException iae) {
			
			assertNull(iae.getMessage());
			
			CommandRuntimeException cre = new CommandRuntimeException("IAE", iae);
			String message = cre.getMessage();
			assertTrue("Contains underlying exceptions message", message.contains("Message: null"));
		}
		
	}
}
