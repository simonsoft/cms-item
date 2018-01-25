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
package se.simonsoft.cms.item.export;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;

public class CmsExportFsWriterSingleTest {
	
	private final Path testExportFilePath = Paths.get("se/simonsoft/cms/item/export/export-test.txt");
	private final int testExportFileSize = 19;
	private final String tmpDir = "tempFsExportArea";
	
	@Test
	public void testExportSingleFileAsZip() throws Exception {
		
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
		
		File resultFile = new File(tempDir.toAbsolutePath().toString().concat("/" + j.getJobPath()));
		assertTrue(resultFile.exists());
		
		unzipPackage(resultFile.getAbsolutePath());
		
		Path unzipPackagePath = Paths.get(resultFile.getParent().concat(cmsExportPath.getPath()));
		assertTrue(Files.exists(unzipPackagePath));
		assertEquals(testExportFileSize, Files.size(unzipPackagePath));
	}
	
	@Test
	public void testImportSingleFileAsZip() throws Exception {
		
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
		
		final String patternString = "Provided import path:.*, do not exist";
        Pattern pattern = Pattern.compile(patternString);
        
		try {
			fsReaderSingle.prepare(importJob);
			fail("Should fail provided path do not exist");
		} catch (IllegalStateException e) {
			Matcher matcher = pattern.matcher(e.getMessage());
			assertTrue(matcher.matches());
		}
	}
	
	public void unzipPackage(String exportPath) throws IOException {

        ZipInputStream unzip = new ZipInputStream(new FileInputStream(exportPath));
        ZipEntry ze = unzip.getNextEntry();
        File newFile = null;

        while (ze != null) {
            String fileName = ze.getName();

            String parentFolder = exportPath.substring(0 ,exportPath.lastIndexOf("/"));

            newFile = new File(parentFolder + File.separator + fileName);

            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = unzip.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            ze = unzip.getNextEntry();
        }

        unzip.closeEntry();
        unzip.close();

    }
}