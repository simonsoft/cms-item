package se.simonsoft.cms.item;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import se.simonsoft.cms.item.Checksum.Algorithm;

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
	public void testToString() {
		Checksum c1 = new ChecksumRead();
		assertTrue("got: " + c1, c1.toString().contains("MD5=d41d8cd98f00b204e9800998ecf8427e"));
		assertTrue("got: " + c1, c1.toString().contains("SHA1=da39a3ee5e6b4b0d3255bfef95601890afd80709"));
	}
	
}
