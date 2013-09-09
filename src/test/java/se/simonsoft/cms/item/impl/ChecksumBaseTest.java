/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import se.simonsoft.cms.item.Checksum;
import se.simonsoft.cms.item.Checksum.Algorithm;
import se.simonsoft.cms.item.impl.ChecksumBase;
import se.simonsoft.cms.item.impl.ChecksumRead;

public class ChecksumBaseTest {

	@Test
	public void testNamedGetters() {
		ChecksumBase c = new ChecksumBase() {
			@Override
			public boolean has(Algorithm algorithm) {
				return true;
			}
			@Override
			public String getHex(Algorithm algorithm) {
				return "get" + algorithm.toString().toLowerCase();
			}
		};
		assertEquals("getmd5", c.getMd5());
		assertEquals("getsha1", c.getSha1());
	}
	
	@Test
	public void testEqualsBoth() throws IOException {
		InputStream source = new ByteArrayInputStream("testing\n".getBytes());
		Checksum c1 = new ChecksumRead().add(source);
		Checksum c2 = mock(Checksum.class);
		when(c2.has(Algorithm.MD5)).thenReturn(true);
		when(c2.has(Algorithm.SHA1)).thenReturn(true);
		when(c2.getHex(Algorithm.MD5)).thenReturn(c1.getMd5());
		when(c2.getHex(Algorithm.SHA1)).thenReturn(c1.getSha1());
		assertTrue("Should be equal because both checksums exist and match", c1.equals(c2));
	}
	
	@Test
	public void testEqualsNone() throws IOException {
		Checksum c1 = new ChecksumRead(new Algorithm[]{});
		Checksum c2 = new ChecksumRead(new Algorithm[]{});
		assertFalse("At least one checksum is required for equals", c1.equals(c2));
	}

	@Test
	public void testEqualsKnown() throws IOException {
		InputStream source = new ByteArrayInputStream("testing\n".getBytes());
		Checksum c1 = new ChecksumRead().add(source);
		Checksum c2 = mock(Checksum.class);
		when(c2.has(Algorithm.MD5)).thenReturn(true);
		when(c2.has(Algorithm.SHA1)).thenReturn(false);
		when(c2.getHex(Algorithm.MD5)).thenReturn(c1.getMd5());
		assertFalse("Should not be strictly equals because algorithm set does not match", c1.equals(c2));
		assertTrue("Should be equalsKnown because the intersection of algorithms matches", c1.equalsKnown(c2));
		Checksum c3 = mock(Checksum.class);
		when(c3.has(Algorithm.MD5)).thenReturn(false);
		when(c3.has(Algorithm.SHA1)).thenReturn(true);
		when(c3.getHex(Algorithm.MD5)).thenReturn(c1.getMd5());
		when(c3.getHex(Algorithm.SHA1)).thenReturn(c1.getSha1());
		assertTrue("Should be equalsKnown because the intersection of algorithms matches", c1.equalsKnown(c3));
		Checksum c4 = mock(Checksum.class);
		when(c4.has(Algorithm.MD5)).thenReturn(false);
		when(c4.has(Algorithm.SHA1)).thenReturn(false);
		assertFalse("Should not be equals because there is no algorithm overlap", c1.equalsKnown(c4));
	}	
	
	@Test
	public void testToString() {
		Checksum c1 = new ChecksumRead();
		assertTrue("got: " + c1, c1.toString().contains("MD5=d41d8cd98f00b204e9800998ecf8427e"));
		assertTrue("got: " + c1, c1.toString().contains("SHA1=da39a3ee5e6b4b0d3255bfef95601890afd80709"));
	}
	
	@Test
	public void testEqualsThird() throws IOException {
		InputStream source = new ByteArrayInputStream("testing\n".getBytes());
		Checksum c1 = new ChecksumRead(Algorithm.MD5, Algorithm.SHA1, Algorithm.SHA256).add(source);
		Checksum c2 = mock(Checksum.class);
		when(c2.has(Algorithm.MD5)).thenReturn(true);
		when(c2.has(Algorithm.SHA1)).thenReturn(true);
		when(c2.getHex(Algorithm.MD5)).thenReturn(c1.getMd5());
		when(c2.getHex(Algorithm.SHA1)).thenReturn(c1.getSha1());
		// this is for backwards compatibility, but also reasonable with respect to the low risk of collitions when two algorithms are used		
		assertTrue("Should be equals even though the algorithm set differs because the default set of algorithms are same", c1.equals(c2));
		assertTrue(c1.equalsKnown(c2));
		Checksum c3 = mock(Checksum.class);
		when(c3.has(Algorithm.MD5)).thenReturn(true);
		when(c3.has(Algorithm.SHA1)).thenReturn(true);
		when(c3.getHex(Algorithm.MD5)).thenReturn(c1.getMd5());
		when(c3.getHex(Algorithm.SHA1)).thenReturn(c1.getSha1());
		when(c3.has(Algorithm.SHA256)).thenReturn(true);
		when(c3.getHex(Algorithm.SHA256)).thenReturn("someothercheksum");
		assertFalse("Should not be equal because there is an algorithm with different checksums", c1.equals(c3));
	}
	
}
