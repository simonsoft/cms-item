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


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import se.simonsoft.cms.item.export.CmsExportItemInputStream;
import se.simonsoft.cms.item.export.CmsExportPath;
import se.simonsoft.cms.item.export.CmsExportPrefix;

public class CmsExportFsWriterSingleTest {
	
	public static String TEST_FILE_PATH = "se/simonsoft/cms/item/export/export-test.txt";
	
	
	@Test
	public void testExportSingleFileAsZip() throws Exception {
		
		Path tempDir = Files.createTempDirectory("tempFsExportArea");
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(TEST_FILE_PATH);
		
		CmsExportWriterFsSingle writer = new CmsExportWriterFsSingle(tempDir.toFile());
		CmsExportJobZip j = new CmsExportJobZip(new CmsExportPrefix("jandersson"), "export-test", "zip");
		
		CmsExportItemInputStream exportItem = new CmsExportItemInputStream(resourceAsStream, new CmsExportPath("/some/awsome/path.txt"));
		
		j.addExportItem(exportItem);
		j.prepare();
		
		writer.prepare(j);
		writer.write();
		
		File resultFile = new File(tempDir.toAbsolutePath().toString().concat("/" + j.getJobPath()));
		assertTrue(resultFile.exists());
		
	}
}