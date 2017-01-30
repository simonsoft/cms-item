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

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmsExportJobZip extends CmsExportJob implements CmsExportJob.SingleItem{

    private ZipOutputStream zipOutputStream;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    
	public CmsExportJobZip(CmsExportPrefix jobPrefix, String jobName, String jobExtension) {	
		super(jobPrefix, jobName, jobExtension);
	}
	
	public CmsExportJobZip(CmsExportPrefix jobPrefix, String jobName) {	
		super(jobPrefix, jobName, "zip");
	}
    
    @Override
    public void getResultStream(OutputStream out) {

        if (!isReady()) {
            throw new IllegalStateException("The Job is not prepared to be written.");
        }

        if (out == null) {
            throw new IllegalArgumentException("OutputStream");
        }

        try {
            zipOutputStream = new ZipOutputStream(out);

            for (CmsExportItem item : getExportItems()) {
                String path = item.getResultPath().getPath();
                //Path's first "/" must be removed for zips on windows should work.
                ZipEntry entry = new ZipEntry(path.substring(1, path.length()));
                zipOutputStream.putNextEntry(entry);
                item.getResultStream(zipOutputStream);
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.close();

        } catch (IOException e) {
            logger.error("Could not compress to file: {}", e.getMessage());
        }

    }

}



