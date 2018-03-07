package se.simonsoft.cms.item.command;

import static org.junit.Assert.assertEquals;
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
			assertTrue("NPE should be inluded in message", message.contains("java.lang.NullPointerException"));
			assertTrue("This class name should be included", message.contains("CommandRuntimeExceptionTest.java"));
			//Hard to get a stacktrace that exceeds max length.
			assertTrue("Longer messages then 10000 is substringed", message.getBytes().length <= MESSAGE_MAX_LEN);
		}
	}
}
