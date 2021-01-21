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

	@Test
	public void testString() throws IOException {
		
		String source = "Thä string. ";
		
		ByteArrayInOutStream baios = new ByteArrayInOutStream(source);
		
		ByteArrayInputStream is = baios.getInputStream();
		assertNotNull(is);
		
		try {
			baios.write("Fails".getBytes());
			fail("Should not allow more writes");
		} catch (Exception e) {
			assertNull("NPE", e.getMessage());
		}
		
		assertEquals("The T char", 84, is.read());
		
		byte[] buf = new byte[200];
		int read = is.read(buf);
		assertEquals(12, read);
		assertEquals("hä string. ", new String(buf, 0, read));
		
		// Ability to re-read the buffer.
		ByteArrayInputStream is1 = baios.getInputStream();
				
		byte[] buf1 = new byte[200];
		int read1 = is1.read(buf1);
		assertEquals(13, read1);
		assertEquals("Thä string. ", new String(buf1, 0, read1));
				
		baios.close();
	}
}
