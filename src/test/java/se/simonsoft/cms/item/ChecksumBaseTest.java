package se.simonsoft.cms.item;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

import se.simonsoft.cms.item.Checksum.Algorithm;

public class ChecksumBaseTest {

	@Test
	@Ignore // in development
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

}
