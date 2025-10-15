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
package se.simonsoft.cms.item.export;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;

public class CmsExportWriterFsSingleTest {
	
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
		Long contentLength = j.prepare();
		assertNull(contentLength); // CmsExportItemInputStream does not support content length.
		
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
	public void testExportSingleFileString() throws Exception {
		
		Path tempDir = Files.createTempDirectory(tmpDir);
		
		CmsExportWriterFsSingle writer = new CmsExportWriterFsSingle(tempDir.toFile());
		CmsExportJobZip j = new CmsExportJobZip(new CmsExportPrefix("jandersson"), "export-test", "zip");
		
		// Multi-byte chars in string to test charset / size handling.
		CmsExportItemString exportItemString = new CmsExportItemString("Göteborg", new CmsExportPath("/test/string.txt"));
		
		j.addExportItem(exportItemString);
		Long contentLength = j.prepare();
		assertEquals("All items support length", Long.valueOf(9), contentLength); 
		
		writer.prepare(j);
		writer.write();
		
		File resultFile = new File(tempDir.toAbsolutePath().toString().concat("/" + j.getJobPath()));
		assertTrue(resultFile.exists());
		
		unzipPackage(resultFile.getAbsolutePath());
		
		Path unzipPackagePathString = Paths.get(resultFile.getParent().concat(exportItemString.getResultPath().getPath()));
		assertTrue(Files.exists(unzipPackagePathString));
		assertEquals(9, Files.size(unzipPackagePathString));
		String content = new String(Files.readAllBytes(unzipPackagePathString), "UTF-8");
		assertEquals("Göteborg", content);
		unzipPackagePathString = null;
	}
	
	@Test
	public void testExportTwoFiles() throws Exception {
		
		Path tempDir = Files.createTempDirectory(tmpDir);
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(testExportFilePath.toString());
		
		CmsExportWriterFsSingle writer = new CmsExportWriterFsSingle(tempDir.toFile());
		CmsExportJobZip j = new CmsExportJobZip(new CmsExportPrefix("jandersson"), "export-test", "zip");
		
		CmsExportPath cmsExportPath = new CmsExportPath("/test/export-test.txt");
		CmsExportItemInputStream exportItemStream = new CmsExportItemInputStream(resourceAsStream, cmsExportPath);
		
		// Multi-byte chars in string to test charset / size handling.
		CmsExportItemString exportItemString = new CmsExportItemString("Göteborg", new CmsExportPath("/test/string.txt"));
		
		j.addExportItem(exportItemStream);
		j.addExportItem(exportItemString);
		Long contentLength = j.prepare();
		assertNull(contentLength); // CmsExportItemInputStream does not support content length, any item with unknown length makes total length unknown.
		
		writer.prepare(j);
		writer.write();
		
		File resultFile = new File(tempDir.toAbsolutePath().toString().concat("/" + j.getJobPath()));
		assertTrue(resultFile.exists());
		
		unzipPackage(resultFile.getAbsolutePath());
		
		Path unzipPackagePathStream = Paths.get(resultFile.getParent().concat(cmsExportPath.getPath()));
		assertTrue(Files.exists(unzipPackagePathStream));
		assertEquals(testExportFileSize, Files.size(unzipPackagePathStream));
		unzipPackagePathStream = null;
		
		Path unzipPackagePathString = Paths.get(resultFile.getParent().concat(exportItemString.getResultPath().getPath()));
		assertTrue(Files.exists(unzipPackagePathString));
		assertEquals(9, Files.size(unzipPackagePathString));
		unzipPackagePathString = null;
	}
	
	@Test
	public void testExportTwoFilesString() throws Exception {
		
		Path tempDir = Files.createTempDirectory(tmpDir);
		
		CmsExportWriterFsSingle writer = new CmsExportWriterFsSingle(tempDir.toFile());
		CmsExportJobZip j = new CmsExportJobZip(new CmsExportPrefix("jandersson"), "export-test", "zip");
		
		
		// Multi-byte chars in string to test charset / size handling.
		CmsExportItemString exportItemString = new CmsExportItemString("Göteborg", new CmsExportPath("/test/string.txt"));
		
		j.addExportItem(new CmsExportItemString("Ängelholm", new CmsExportPath("/test/string2.txt")));
		j.addExportItem(exportItemString);
		Long contentLength = j.prepare();
		// NOTE: This is unlikely to be used as exact measure since there is currently no multi-item writer (must zip).
		// However, it could be useful for indicating total size for writer to select a suitable type of upload for the size class.
		assertEquals("All items support length", Long.valueOf(9 + 10), contentLength); // Test sum of two known lengths.
		
		writer.prepare(j);
		writer.write();
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