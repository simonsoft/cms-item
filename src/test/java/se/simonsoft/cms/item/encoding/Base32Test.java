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
package se.simonsoft.cms.item.encoding;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.encoding.Base32;

public class Base32Test {

	@Test
	public void testBase32AndBack() {
		Base32 encoding = new Base32();
		long[] numbers = {-200L, 0L, 31L, 32L, -1, -1000L, System.currentTimeMillis(), Long.MAX_VALUE};
		
		for (int i=0; i<numbers.length; i++) {
			String base32 = encoding.encode(numbers[i]);
			assertEquals("Decoding " + base32, numbers[i], encoding.decode(base32));
			
		}
		
	}

	@Test
	public void testGetPadded() {
		Base32 encoding = new Base32();
		assertEquals("" + encoding.getZero() + encoding.getZero() + encoding.getZero(), encoding.encodePad(0, 3));
		assertEquals("012", encoding.encodePad(34, 3));
		assertEquals("12", encoding.encodePad(34, 2));
		try {
			encoding.encodePad(34, 1);
			fail("Should have thrown exception for too long value");
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	@Test
	public void testGetRandom() {
		Base32 encoding = new Base32();
		String r1 = encoding.random(4);
		assertNotNull(r1);
		assertEquals(4, r1.length());
		assertTrue(!r1.equals(encoding.random(4)));
		assertEquals(1, encoding.random(1).length());
	}
	
	@Test
	public void testPadRight() {
		Base32 encoding = new Base32();
		String r1 = encoding.encodePadRightRandom(33, 5);
		assertEquals("11", r1.substring(0, 2));
		assertTrue(!r1.equals(encoding.encodePadRightRandom(33, 5)));
	}
	
}
