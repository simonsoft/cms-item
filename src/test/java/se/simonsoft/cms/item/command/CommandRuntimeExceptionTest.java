package se.simonsoft.cms.item.command;

import static org.junit.Assert.*;

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
			assertTrue("Conatins underlying exceptions message", message.contains("Message: Underlying exceptions message"));
		}
		
	}
}
