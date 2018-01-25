package se.simonsoft.cms.item.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class CmsExportFsReaderSingleTest {
	
	
	private final Path testExportFilePath = Paths.get("se/simonsoft/cms/item/export/export-test.txt");
	private final String tmpDir = "tempFsExportArea";
	
	
	@Test
	public void testImportSingleFileAsZip() throws Exception {
		
		//Exporting file with our fs export api.
		Path tempDir = Files.createTempDirectory(tmpDir);
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(testExportFilePath.toString());
		
		CmsExportWriterFsSingle writer = new CmsExportWriterFsSingle(tempDir.toFile());
		CmsExportJobZip j = new CmsExportJobZip(new CmsExportPrefix("jandersson"), "export-test", "zip");
		
		CmsExportPath cmsExportPath = new CmsExportPath("/test/export-test.txt");
		CmsExportItemInputStream exportItem = new CmsExportItemInputStream(resourceAsStream, cmsExportPath);
		
		j.addExportItem(exportItem);
		j.prepare();
		
		writer.prepare(j);
		writer.write();
		
		CmsImportJobSingle importJob = new CmsImportJobSingle(new CmsExportPrefix("jandersson"), "export-test", "zip");
		
		CmsExportFsReaderSingle fsReaderSingle = new CmsExportFsReaderSingle(tempDir.toFile());
		fsReaderSingle.prepare(importJob);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		fsReaderSingle.getContents(baos);
		
		assertEquals("Zipped job size", 175, baos.size());
		
	}
	
	@Test
	public void testImportJobDoNotExists() throws Exception {
		
		Path tempDir = Files.createTempDirectory(tmpDir);
		
		CmsImportJobSingle importJob = new CmsImportJobSingle(new CmsExportPrefix("jandersson"), "export-test", "zip");

		CmsExportFsReaderSingle fsReaderSingle = new CmsExportFsReaderSingle(tempDir.toFile());
		
		final String patternString = "Import path does not exist:.*";
        Pattern pattern = Pattern.compile(patternString);
        
		try {
			fsReaderSingle.prepare(importJob);
			fail("Should fail provided path do not exist");
		} catch (IllegalStateException e) {
			Matcher matcher = pattern.matcher(e.getMessage());
			assertTrue(matcher.matches());
		}
	}
	
	@Test
	public void testReaderIsNotPrepared() throws Exception {
		
		Path tempDir = Files.createTempDirectory(tmpDir);
		CmsExportFsReaderSingle fsReaderSingle = new CmsExportFsReaderSingle(tempDir.toFile());
		
		try {
		fsReaderSingle.getContents();
		} catch (IllegalStateException e) {
			assertEquals("CmsExportFsReaderSingle has to be prepared before getting content.", e.getMessage());
		}
		
	}
}
