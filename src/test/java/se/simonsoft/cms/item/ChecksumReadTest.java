package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import se.simonsoft.cms.item.Checksum.Algorithm;

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
		Checksum c = new ChecksumRead().add(source);
		assertEquals("eb1a3227cdc3fedbaec2fe38bf6c044a", c.getMd5());
		assertEquals("9801739daae44ec5293d4e1f53d3f4d2d426d91c", c.getSha1());
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
