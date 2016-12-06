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
package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import se.simonsoft.cms.item.Checksum;
import se.simonsoft.cms.item.Checksum.Algorithm;
import se.simonsoft.cms.item.impl.ChecksumRead;

public class ChecksumReadTest {

	@Test
	public void testChecksumImpl() { // should not throw checked exception
		assertTrue(new ChecksumRead().has(Algorithm.MD5));
		assertTrue(new ChecksumRead().has(Algorithm.SHA1));
	}

	@Test
	public void testChecksumImplSubset() {
		Checksum md5 = new ChecksumRead(Algorithm.MD5);
		assertTrue(md5.has(Algorithm.MD5));
		assertFalse(md5.has(Algorithm.SHA1));
	}

	@Test
	public void testInputStream() throws IOException {
		InputStream source = new ByteArrayInputStream("testing\n".getBytes());
		ChecksumRead c = new ChecksumRead().add(source);
		assertEquals("eb1a3227cdc3fedbaec2fe38bf6c044a", c.getMd5());
		try {
			c.add(source);
			fail("Should not allow content to be added after checksum getter has been called");
		} catch (IllegalStateException e) {
			// expected
		}
		assertEquals("9801739daae44ec5293d4e1f53d3f4d2d426d91c", c.getSha1());
		// java MessageDigest resets checksum at digest() so this assert is indeed needed:
		assertEquals("same value", "eb1a3227cdc3fedbaec2fe38bf6c044a", c.getMd5());
		assertEquals("same value", "9801739daae44ec5293d4e1f53d3f4d2d426d91c", c.getSha1());
		// If we were to support subsequent add this would be the test:
		//InputStream source2 = new ByteArrayInputStream("test2\n".getBytes());
		//c.add(source2);
		//assertEquals("new value", "4f3c4e23c1f576a6c77b14619538c810", c.getMd5());
		//assertEquals("new value", "f29d1cbe996c78292a32e0a9c79982cb57f6591d", c.getSha1());
	}
	
	@Test
	public void testFile() throws IOException {
		File f = File.createTempFile(this.getClass().getName(), "");
		FileWriter fw = new FileWriter(f);
		fw.write("xyz\n");
		fw.close();
		Checksum c = new ChecksumRead().add(f);
		assertEquals("b6273b589df2dfdbd8fe35b1011e3183", c.getMd5());
		assertEquals("8714e0ef31edb00e33683f575274379955b3526c", c.getSha1());
	}

	@Test
	public void testConsecutiveAdds() throws IOException {
		InputStream source1 = new ByteArrayInputStream("test".getBytes());
		InputStream source2 = new ByteArrayInputStream("ing\n".getBytes());
		assertEquals("9801739daae44ec5293d4e1f53d3f4d2d426d91c", 
				new ChecksumRead().add(source1).add(source2).getSha1());
	}
	
	@Test
	public void testEqualsUsingRawDigest() {
		// TODO would be elegant and a minor performance improvement to compare the byte arrays,
		// if available, before conversion to string array 
	}

}
