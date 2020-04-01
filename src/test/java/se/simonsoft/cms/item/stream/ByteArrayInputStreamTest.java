package se.simonsoft.cms.item.stream;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class ByteArrayInputStreamTest {

	@Test
	public void test() throws IOException {
		
		ByteArrayInOutStream baios = new ByteArrayInOutStream(100);
		
		String test = "Smaller thän buffer. ";
		assertEquals(21, test.length());
		
		baios.write(test.getBytes());
		assertEquals("Extended char takes extra byte in UTF-8", 21 + 1, baios.size());
		
		ByteArrayInputStream is = baios.getInputStream();
		
		try {
			baios.write("Fails".getBytes());
			fail("Should not allow more writes");
		} catch (Exception e) {
			assertNull("NPE", e.getMessage());
		}
		
		assertEquals("The S char", 83, is.read());
		
		byte[] buf = new byte[200];
		int read = is.read(buf);
		assertEquals(21, read);
		assertEquals("maller thän buffer. ", new String(buf, 0, read));
		
		// Ability to re-read the buffer.
		ByteArrayInputStream is2 = baios.getInputStream();
		
		byte[] buf2 = new byte[200];
		int read2 = is2.read(buf2);
		assertEquals(22, read2);
		assertEquals("Smaller thän buffer. ", new String(buf2, 0, read2));
		
		baios.close();
	}

}
