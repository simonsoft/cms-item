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

import org.junit.Test;

import se.simonsoft.cms.item.export.CmsExportItemInputStream;
import se.simonsoft.cms.item.export.CmsExportPath;
import se.simonsoft.cms.item.export.CmsExportPrefix;

public class CmsExportFsWriterSingleTest {

	
	@Test
	public void testExportSingelFile() throws Exception {
		
		File parentFolder = new File("/Users/jonand/Desktop/test");
		String testFilePath = "se/simonsoft/cms/item/export/DOC_900108_sv-SE_Released.pdf";
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(testFilePath);
		
		
		CmsExportFsWriterSingle writer = new CmsExportFsWriterSingle(parentFolder);
		CmsExportJobZip j = new CmsExportJobZip(new CmsExportPrefix("jandersson"), "DOC_900108_Released", "zip");
		
		CmsExportItemInputStream exportItem = new CmsExportItemInputStream(resourceAsStream, new CmsExportPath("/some/awsome/path.pdf"));
		
		j.addExportItem(exportItem);
		j.prepare();
		
		writer.prepare(j);
		writer.write();
		
		
		File file2 = new File(parentFolder.getAbsolutePath().concat("/" + j.getJobPath()));
		assertTrue(file2.exists());
		
	}
}